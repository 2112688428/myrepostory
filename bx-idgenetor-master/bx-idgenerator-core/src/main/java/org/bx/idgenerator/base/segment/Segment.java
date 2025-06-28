package org.bx.idgenerator.base.segment;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.base.segment.bean.IDWrapper;
import org.bx.idgenerator.exception.BizException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Slf4j
public class Segment {
    private long minId;
    private volatile long curId;
    private long maxId;
    private static final long CUR_ID_OFFSET;
    private static final Unsafe UNSAFE;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
            CUR_ID_OFFSET = UNSAFE.objectFieldOffset(Segment.class.getDeclaredField("curId"));
        } catch (Exception e) {
            throw new BizException(e.getMessage(),e);
        }
    }

    public IDWrapper nextID(int num) {
        IDWrapper idWrapper = null;
        for (; ; ) {
            long curId = this.curId;
            long nextId = curId + num;
            if (curId == maxId) {
                break;
            }
            if (maxId >= nextId && compareAndSwapCurId(curId, nextId)) {
                idWrapper = new IDWrapper();
                idWrapper.setCurId(curId);
                idWrapper.setMinId(curId);
                idWrapper.setMaxId(nextId);
                break;
            }
            long diff = maxId - curId;
            if (nextId > maxId && diff > 0 && compareAndSwapCurId(curId, maxId)) {
                idWrapper = new IDWrapper();
                idWrapper.setCurId(curId);
                idWrapper.setMinId(curId);
                idWrapper.setMaxId(maxId);
                break;
            }
        }
        return idWrapper;
    }

    public BigDecimal usedPercent() {
        BigDecimal bigDecimal;
        //初始状态
        if (maxId == 0) {
            bigDecimal = new BigDecimal(1.00);
        } else {
            bigDecimal = new BigDecimal(curId - minId).divide(new BigDecimal(maxId - minId), 3, RoundingMode.UP);
        }
        return bigDecimal;
    }

    private boolean compareAndSwapCurId(long curId, long nextId) {
        return UNSAFE.compareAndSwapLong(this, CUR_ID_OFFSET, curId, nextId);
    }

}
