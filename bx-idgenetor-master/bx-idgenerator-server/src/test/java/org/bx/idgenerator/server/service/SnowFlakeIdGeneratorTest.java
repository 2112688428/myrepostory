package org.bx.idgenerator.server.service;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.core.server.IdGetterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 雪花算法生成器 TEST
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月08日 10时59分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@RunWith(SpringRunner.class)
//@SpringBootTest
@Slf4j
public class SnowFlakeIdGeneratorTest {


    private static final Integer THREADS = 10000;

    @Autowired
    @Qualifier("snowflakeServiceImpl")
    private IdGetterService snowflakeServiceImpl;


    /**
     * 多线程测试
     *
     * @throws InterruptedException
     */
    @Test
    public void genIdMultiTest() throws InterruptedException, ExecutionException {
        Map<Long, Long> container = new ConcurrentHashMap<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(THREADS);
        List<Future<Long>> list = new ArrayList<>();
        for (int i = 0; i < THREADS; i++) {
            list.add(threadPool.submit(() -> {
                long id = snowflakeServiceImpl.getId(null);
                System.out.println("id: " + id);
                return id;
            }));
            log.info("提交任务完毕");
        }
        for (Future<Long> future : list) {
            Long aLong = future.get();
            container.put(aLong, aLong);
        }
        assert container.size() == THREADS;
        log.info("测试完毕...");
        threadPool.awaitTermination(20, TimeUnit.HOURS);
        threadPool.shutdown();
    }


    /**
     * 多Server节点初始化测试
     * 10台机器（不同ip、不同port）同时启动，获取workerId
     * 保证每台机器获取到的id是唯一的
     *
     * @throws InterruptedException
     */
    @Test
    public void multiHostInitTest() throws InterruptedException, ExecutionException {
//        int TestCount = 10;
//        CountDownLatch countDownLatch = new CountDownLatch(TestCount);
//        String[] hostList = new String[]{
//                "192.168.1.0", "192.168.1.1", "192.168.1.2", "192.168.1.3", "192.168.1.4",
//                "192.168.1.5", "192.168.1.6", "192.168.1.7", "192.168.1.8", "192.168.1.9",
//        };
//        Random random = new Random();
//        ExecutorService threadPool = Executors.newFixedThreadPool(TestCount);
//        CopyOnWriteArrayList<SnowFlakeIdGenerator> objects = Lists.newCopyOnWriteArrayList();
//        for (int i = 0; i < TestCount; i++) {
//            final int[] arry = {i};
//            threadPool.submit(() -> {
//                SnowFlakeIdGenerator generator = new SnowFlakeIdGenerator(hostList[arry[0]], "9999", client, mailService);
//                objects.add(generator);
//                try {
//                    countDownLatch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//            countDownLatch.countDown();
//        }
//        threadPool.awaitTermination(10, TimeUnit.SECONDS);
//        objects.stream().forEach(e -> {
//            long result = e.genId("test");
//            System.out.println(result);
//        });
//        threadPool.shutdown();

    }


//    /**
//     * 查看当前节点数量
//     * @throws InterruptedException
//     * @throws ExecutionException
//     */
//    @Test
//    public void catEtcdData() throws InterruptedException, ExecutionException {
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        ByteSequence key = ByteSequence.from(Constants.PATH_FOREVER, Constants.CHARSET_UTF_8);
//        GetResponse response = kvClient.get(key, GetOption.newBuilder().withPrefix(key).build()).get();
//        List<KeyValue> kvs = response.getKvs();
//        for (KeyValue kv : kvs){
//            System.out.println(String.format("key:%s,value:%s,version:%s",kv.getKey().toString(Constants.CHARSET_UTF_8),kv.getValue().toString(Constants.CHARSET_UTF_8),kv.getVersion()));
//        }
//        key = ByteSequence.from(Constants.PATH_TEMP, Constants.CHARSET_UTF_8);
//        response = kvClient.get(key, GetOption.newBuilder().withPrefix(key).build()).get();
//        kvs = response.getKvs();
//        for (KeyValue kv : kvs){
//            System.out.println(String.format("key:%s,value:%s,version:%s",kv.getKey().toString(Constants.CHARSET_UTF_8),kv.getValue().toString(Constants.CHARSET_UTF_8),kv.getVersion()));
//        }
//    }


    /**
     * 清空持久节点数据
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
//    @Test
//    public void etcCleardForeverData() throws InterruptedException, ExecutionException {
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        ByteSequence key = ByteSequence.from(Constants.PATH_FOREVER, Constants.CHARSET_UTF_8);
//        DeleteResponse deleteResponse = kvClient.delete(key, DeleteOption.newBuilder()
//                .withPrefix(key).build()).get();
//        long deleted = deleteResponse.getDeleted();
//        System.out.println("删除持久节点数: " + deleted);
//
//        key = ByteSequence.from(Constants.PATH_TEMP, Constants.CHARSET_UTF_8);
//        deleteResponse = kvClient.delete(key, DeleteOption.newBuilder()
//                .withPrefix(key).build()).get();
//        deleted = deleteResponse.getDeleted();
//        System.out.println("删除临时节点数: " + deleted);
//    }
    @Test
    public void httpClientTest() throws InterruptedException, ExecutionException {
        String time = HttpUtil.get("http://127.0.0.1:8081/api/time");
        System.out.println(time);
    }

}