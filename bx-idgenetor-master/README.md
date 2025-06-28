```
### ID 生成器

1. Segment 双缓冲DB生成ID
2. SnowFlake 雪花ID



### Segment ID

1. 基于 mysql 的 乐观锁 更新 上限值ID

​```
        //乐观更新
        LambdaUpdateWrapper<LeafNode> eq = new UpdateWrapper<LeafNode>().lambda()
                .eq(LeafNode::getSystemId, sysId)
                .eq(LeafNode::getBizTag, bizTag)
                .eq(LeafNode::getMaxId, maxId);
        // TODO 重试更新
        int i = 0;
        boolean update = false;
        while (i++ < MAX_TRIES && !update) {
            update = leafNodeService.update(leafNode, eq);
        }

        if (!update) {
            throw new BizException("更新失败");
        }

​```

2. 基于 CAS 更新双缓冲区

​```
    
    public IDWrapper nextID(int num) {
        checkException();
        checkSegment();
        IDWrapper idWrapper;
        for (; ; ) {
            idWrapper = curSegment.nextID(num);
            if (idWrapper == null) {
                // wait until next segment ok
                checkSegment();
                for (; ; ) {
                    checkException();
                    if (state == NORMAL) {
                        break;
                    }
                    // cas 配置
                    if (state == FILLED_NEXT_BUFFER && nextSegment != null && compareAndSwapState(FILLED_NEXT_BUFFER, CHANGE_NEXT_BUFFER)) {
                        log.info("{},next segment ok", tag);
                        changeSegmentAndNotify();
                        break;
                    }
                    synchronized (this) {
                        try {
                            //等待填充
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                break;
            }
        }
        return idWrapper;
    }

​```

3. 基于 Guava 的 事件通知 异步处理 FillSegmentEvent 事件

> 发送填充事件

​```
    /**
     * 检查是否达到切换填充下一个缓冲的比例
     */
    private void checkSegment() {
        // 达到填充阈值 且 下一个缓冲区为空 且 没有存在争用的状态下
        if (curSegment.usedPercent().compareTo(iFetchPolicy.nextSegFetchPercent(this.tag)) >= 0
                && nextSegment == null
                && compareAndSwapState(NORMAL, FILLING_NEXT_BUFFER)) {
            // 构建填充消息
            log.info("开始填充:{}", tag);
            FillSegmentEvent fillSegmentEvent = new FillSegmentEvent(this, this.leafInfoService, iFetchPolicy.segmentFetchSize(this.tag), tag);
            fillSegmentUpdater.notifyFill(fillSegmentEvent);
        }
    }

​```

> 监听处理事件

​```
    /**
     * 填充 nextSegment
     *
     * @param event 消费填充缓冲区事件
     */
    @Subscribe
    public void fill(FillSegmentEvent event) {
        log.info("FillSegmentListener接收到的事件:{}", event);
        SegmentBuffer segmentBuffer = event.getSegmentBuffer();
        Segment segment = null;
        Throwable e = null;
        try {
            //获得节点信息
            LeafInfo leafInfo = event.getLeafInfoService().getLeafInfo(event.getTag());
            log.info("FillSegmentListener  处理结果:{}", leafInfo);
            segment = leafInfoToSegment(leafInfo);
        } catch (Exception exception) {
            e = exception;
            log.error("FillSegmentListener处理出现异常", e);
        }

        segmentBuffer.setNextSegment(segment);
        // 设置发生异常
        segmentBuffer.fillComplete(e);
    }


​```

4. 基于 ThreadLocal 的 线程缓冲区 提升生产ID吞吐量与响应速度。但是目前存在一些问题，

> 1. 由于我们在内存里做的基于ThreadLocal的 工作线程 `WorkerThread` 和`SegmentBuffer`的映射
>    EnhanceThreadLocal<WorkerThread,SegmentBuffer>。如果线程池的核心线程数core和最大线程数max不一样，
>    那么我们的工作线程 `WorkerThread`可能会被回收掉，但是线程池并没有提供
>    线程被回收的钩子函数，导致EnhanceThreadLocal中映射没有被及时清除;如果有钩子函数，那么我们要考虑有没有必要
>    将`SegmentBuffer`中没有及时消费掉的ID进行回收（对节点的maxId进行回收），会比较复杂。

> 2. 由于线程池中的工作线程，如果处理任务中出现异常，那么会导致该工作线程被回收掉，那么上面讲到的 工作线程 `WorkerThread` 和`SegmentBuffer`的映射
>    会失效，所以我们可以在我们的业务端将异常catch住，不抛出运行时的异常。但是我们可以把异常信息作为预警消息发给相关人员。这种方案也可行。
>    但是如果异常是由于硬件或环境层面导致的，是无法处理的，如果出现了这种异常，那也意味着我们的环境出现了问题，需要修复。





### SnowFlake ID

> SnowFlake的ID生成器我们参考了 `美团Leaf` 的ID设计，依赖中间件改为
> 后起之秀的分布式组件 ETCD，执行服务器之间的时间校准和机器ID分配

> ID格式设计

​```
  基于雪花算法的ID生成器
  64bit固定长度，具体分段如下:
  +--------+-----------------------------------------------+------------+---------------+
  | 1bit   |               41bit                           |  10bit     |     12bit     |
  +--------+-----------------------------------------------+------------+---------------+
  |   0    |00000000 00000000 00000000 00000000 00000000 0 |00000 00000 |00000 00000 00 |
  +--------+-----------------------------------------------+------------+---------------+
  | no used|              TimeStamp                        | WorkerId   |  SequenceId   |
  +------+---+---------------------------------------------+------------+---------------+

​```


#### 接入文档

1.目前只支持segment式获取id，所以接入之前必须要插入所要使用segment的必要信息：系统id，业务标识，步长
    测试环境admin：http://172.20.0.237:8081
    账号：admin
    密码：123
    点击节点管理，添加节点信息，注意 系统标识，业务标识和步长不能为空
2.maven地址：

​```
        <dependency>
            <groupId>org.bx</groupId>
            <artifactId>bx-idgenerator-client</artifactId>
            <version>1.0.0.RELEASE</version>
        </dependency>
​```

3.目前id服务提供的客户端只支持restful风格获取id,请在启动类上加上扫描路径:org.bx。
示例:

​```
    @Autowired
    IdGenerateServiceWrapper wrapper;

    public ResultWrapper<String> test(){
        
        IdGenerateBean idGenerateBean = new IdGenerateBean();
        //这是自己在id生成器客户端添加的节点信息中的---系统标识
        idGenerateBean.setSystemId("ks");
        //这是想要获取的id位数,默认为8
        idGenerateBean.setSize(10);
        //这是自己在id生成器客户端添加的节点信息中的---业务标识
        idGenerateBean.setBizTag("pay");
        org.bx.idgenerator.bean.ResultWrapper<String> result = wrapper.getSegmentId(idGenerateBean);

        int code = result.getCode();
        if(code != 200){
            return ResultWrapper.getErrorResultWrapper(result.getMsg());

        }
      
        return ResultWrapper.getSuccessResultWrapper(result.getData());

    }

​```

IdGenerateServiceWrapper 这个是包装了一层的id客户端，其中集成了钉钉机器人的告警，
当出现异常的时候进行钉钉机器人告警，钉钉机器人依赖的参数：

​```
ddrobot:
  secret: SECc5aa51e143bfbaf6b0384d37d2f80cb3281fcf3222a6e2d0cc1a162df9d2c389
  hookurl: https://oapi.dingtalk.com/robot/send?access_token=b4a9f19c6a94f40d461593bec7001ce393b9a2bb63dd30f9f9f2f9943f270d19

​```

当然接入方不一定要使用IdGenerateServiceWrapper,可以自己进行封装适合自己的id客户端,以便进行扩展
```