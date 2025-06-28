package org.bx.idgenerator.base.segment.policy;

import java.math.BigDecimal;

/**
 * 缓存id的拉取策略
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月06日 12时28分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public interface IFetchPolicy {

    /**
     * 是否开启threadLocal缓存
     * @return
     */
    boolean threadLocalCacheEnabled();

    /**
     * threadLocal缓存一次拉取的数量
     * @param key
     * @return
     */
    int threadLocalFetchSize(String key);

    /**
     * segment一次拉取的数量
     * @param key
     * @return
     */
    int segmentFetchSize(String key);

    /**
     * 当前segment使用超过此比例的是时候,会填充下一个segment
     * @param key
     * @return
     */
    BigDecimal nextSegFetchPercent(String key);


}
