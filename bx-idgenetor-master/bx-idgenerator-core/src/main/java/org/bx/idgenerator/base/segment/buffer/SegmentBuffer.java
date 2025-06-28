package org.bx.idgenerator.base.segment.buffer;

import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.base.segment.ILeafInfoService;
import org.bx.idgenerator.base.segment.Segment;
import org.bx.idgenerator.base.segment.bean.IDWrapper;
import org.bx.idgenerator.base.segment.bus.FillSegmentEvent;
import org.bx.idgenerator.base.segment.bus.FillSegmentUpdater;
import org.bx.idgenerator.base.segment.policy.IFetchPolicy;
import org.bx.idgenerator.exception.BizException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;


/**
 * ID段缓冲
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020年07月09日 15时54分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Slf4j
public class SegmentBuffer {
    public static final int CHANGE_NEXT_BUFFER = 3;
    public static final int FILLED_NEXT_BUFFER = 2;
    public static final int FILLING_NEXT_BUFFER = 1;
    public static final int NORMAL = 0;
    private volatile int state = NORMAL;
    /**
     * 当前段缓存
     */
    private volatile Segment curSegment;
    /**
     * next 段缓存
     */
    private volatile Segment nextSegment;
    private final ILeafInfoService leafInfoService;
    private static final long STATE_OFFSET;
    private static final Unsafe UNSAFE;
    private final FillSegmentUpdater fillSegmentUpdater;
    private final IFetchPolicy iFetchPolicy;
    private final String tag;
    private Throwable e;
    private static final String THE_SAFE = "theUnsafe";

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField(THE_SAFE);
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
            STATE_OFFSET = UNSAFE.objectFieldOffset(SegmentBuffer.class.getDeclaredField("state"));
        } catch (Exception e) {
            throw new BizException(e.getMessage(), e);
        }
    }

    public SegmentBuffer(ILeafInfoService leafInfoService, FillSegmentUpdater fillSegmentUpdater, IFetchPolicy iFetchPolicy, String tag) {
        this.leafInfoService = leafInfoService;
        this.fillSegmentUpdater = fillSegmentUpdater;
        this.iFetchPolicy = iFetchPolicy;
        this.tag = tag;
        init();
    }

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


    public void setNextSegment(Segment segment) {
        this.nextSegment = segment;
    }

    public void fillComplete(Throwable e) {
        this.e = e;
        state = FILLED_NEXT_BUFFER;
        synchronized (this) {
            this.notify();
        }
    }

    /**
     * 检查填充是否发生异常
     */
    private void checkException() {
        if (e != null) {
            if (e instanceof BizException) {
                throw (BizException) e;
            } else {
                throw new BizException("检测到异常,tag{" + tag + "}", e);
            }
        }
    }

    /**
     * 交换段缓冲
     */
    private void changeSegmentAndNotify() {
        curSegment = nextSegment;
        nextSegment = null;
        state = NORMAL;
        synchronized (this) {
            this.notifyAll();
        }
    }

    private void init() {
        //初始化一个空的
        curSegment = new Segment();
    }

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

    private boolean compareAndSwapState(int curState, int newState) {
        return UNSAFE.compareAndSwapInt(this, STATE_OFFSET, curState, newState);
    }
}
