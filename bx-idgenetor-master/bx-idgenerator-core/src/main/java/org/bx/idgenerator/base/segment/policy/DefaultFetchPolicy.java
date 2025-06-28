package org.bx.idgenerator.base.segment.policy;

import org.bx.idgenerator.properties.SegmentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 缓存id的默认策略
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月06日 12时28分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Component
public class DefaultFetchPolicy implements IFetchPolicy {

    @Autowired
    private SegmentProperties segmentProperties;

    @Override
    public boolean threadLocalCacheEnabled() {
        return segmentProperties.getThreadLocalCacheEnabled();
    }

    @Override
    public int threadLocalFetchSize(String key) {
        return segmentProperties.getThreadLocalFetchSize();
    }

    @Override
    public int segmentFetchSize(String key) {
        return segmentProperties.getSegmentFetchSize();
    }

    @Override
    public BigDecimal nextSegFetchPercent(String key) {
        return segmentProperties.getNextSegmentFetchPercent();
    }

}
