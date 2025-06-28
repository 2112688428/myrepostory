package org.bx.idgenerator.base.segment;

import cn.hutool.core.collection.ConcurrentHashSet;
import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.base.segment.bean.IDWrapper;
import org.bx.idgenerator.base.segment.buffer.SegmentBuffer;
import org.bx.idgenerator.base.segment.bus.FillSegmentUpdater;
import org.bx.idgenerator.base.segment.policy.IFetchPolicy;
import org.bx.idgenerator.core.IDGenerator;
import org.bx.idgenerator.lifecycle.AbstractGeneratorLifecycle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CacheSegmentIDGenerator extends AbstractGeneratorLifecycle implements IDGenerator {
    private final EnhanceThreadLocal threadLocal = new EnhanceThreadLocal();
    private final ConcurrentHashMap<String, SegmentBuffer> segmentBufferMap = new ConcurrentHashMap<>(256);
    private IFetchPolicy iFetchPolicy;
    private ILeafInfoService leafInfoService;
    private FillSegmentUpdater fillSegmentUpdater;
    private final ConcurrentHashSet<Thread> threadSet = new ConcurrentHashSet<>(256);

    public CacheSegmentIDGenerator(ILeafInfoService leafInfoService, FillSegmentUpdater fillSegmentUpdater, IFetchPolicy iFetchPolicy) {
        this.leafInfoService = leafInfoService;
        this.fillSegmentUpdater = fillSegmentUpdater;
        this.iFetchPolicy = iFetchPolicy;
    }

    @Override
    public long genId(String key) {
        return iFetchPolicy.threadLocalCacheEnabled() ? getIdForThreadLocal(key) : getIdForSegment(key);

    }

    /**
     * 从ThreadLocal中获取id
     *
     * TODO (不建议,待优化)
     * 存在线程挂掉之后，无法及时清空segmentBufferMap，
     *
     * @param key
     * @return
     */
    private long getIdForThreadLocal(String key) {
        //查看tl中是否有缓存
        log.info("获取ID,key:{}", key);
        Map<String, IDWrapper> map = threadLocal.get();
        IDWrapper idWrapper = map.get(key);
        if (isFill(idWrapper)) {
            log.info("tl中无缓存，开始填充缓存,key:{}", key);
            SegmentBuffer segmentBuffer = segmentBufferMap.get(key);
            if (segmentBuffer == null) {
                //若二级缓存没有,则开始初始化
                synchronized (this) {
                    if ((segmentBuffer = segmentBufferMap.get(key)) == null) {
                        log.info("二级缓存中无缓存，开始填充缓存,key:{}", key);
                        //这里的maxid curid 是初始值0
                        segmentBuffer = new SegmentBuffer(leafInfoService, fillSegmentUpdater, iFetchPolicy, key);
                        segmentBufferMap.put(key, segmentBuffer);
                    }
                }
            }
            boolean hasException = false;
            try {
                idWrapper = segmentBuffer.nextID(iFetchPolicy.threadLocalFetchSize(key));
            } catch (Exception e) {
                hasException = true;
                //捕捉到异常 取消缓存
                log.info("获取id异常：{}", e);
                //幂等删除
                segmentBufferMap.remove(key);
                throw e;
            } finally {
                if (!threadSet.contains(Thread.currentThread())) {
                    threadSet.add(Thread.currentThread());
                }
                if (!hasException) {
                    map.put(key, idWrapper);
                }
            }
        }
        long curId = idWrapper.getCurId();
        idWrapper.setCurId(curId + 1);
        return curId;
    }
    
    private long getIdForSegment(String key) {
        SegmentBuffer segmentBuffer = segmentBufferMap.get(key);
        if (segmentBuffer == null) {
            //若二级缓存没有,则开始初始化
            synchronized (this) {
                if ((segmentBuffer = segmentBufferMap.get(key)) == null) {
                    log.info("二级缓存中无缓存，开始填充缓存,key:{}", key);
                    //这里的 maxid curid 是初始值0
                    segmentBuffer = new SegmentBuffer(leafInfoService, fillSegmentUpdater, iFetchPolicy, key);
                    segmentBufferMap.put(key, segmentBuffer);
                }
            }
        }
        try {
            IDWrapper idWrapper = segmentBuffer.nextID(1);
            return idWrapper.getCurId();
        } catch (Exception e) {
            //捕捉到异常 取消缓存
            log.info("获取id异常,取消sgement缓存,等待下一次填充：{}", e);
            //幂等删除
            segmentBufferMap.remove(key);
            throw e;
        }
    }



    private boolean isFill(IDWrapper idWrapper) {
        //左开右闭
        return idWrapper == null || idWrapper.getCurId() > idWrapper.getMaxId() - 1;
    }

    /**
     * 删除相关键的缓存
     *
     * @return
     * @deprecated 这个方法有隐患
     */
    @Deprecated
    public boolean deleteSegment(String key) {
        //循环删除
        threadSet.stream().forEach(thread -> {
            Map<String, IDWrapper> value = threadLocal.getValue(thread);
            if (value != null) {
                if (value.remove(key) != null) {
                    log.info("thread: {},removed key: {}", thread, key);
                }
            }
            segmentBufferMap.remove(key);

        });
        return false;

    }

    @Override
    public String name() {
        return "segment generator";
    }


    private static class EnhanceThreadLocal {
        /**
         * 2级缓存
         * key:threadId  value:(key:bzId,value:idInfo)
         */
        private Map<Thread, Map<String, IDWrapper>> map = new ConcurrentHashMap<>(256);

        public Map<String, IDWrapper> get() {
            Thread thread = Thread.currentThread();
            Map<String, IDWrapper> value = map.get(thread);
            if (value == null) {
                value = new HashMap<>(256);
                map.put(thread, value);
            }
            return value;
        }

        public Map<String, IDWrapper> getValue(Thread thread) {
            return map.get(thread);
        }
    }
}
