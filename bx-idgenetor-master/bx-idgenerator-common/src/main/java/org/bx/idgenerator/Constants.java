package org.bx.idgenerator;

import org.bx.idgenerator.bean.IdGenerateBean;
import org.bx.idgenerator.bean.KeyInfoBean;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月06日 19时03分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public class Constants {

    public static final String SNOWFLAKE_LOCAL_WORKERID = "workerID";
    public static final String IDGENERATOR_NAME = "bx.idgenerator.name";
    // public static final String IDGENERATOR_NAME_VALUE               = PropertyFactory.getProperty(IDGENERATOR_NAME);
    //public static final String SNOWFLAKE_BLOCKBACKT_HRESHOLD        = "bx.snowflake.blockBackThreshold";
    //public static final long SNOWFLAKE_BLOCKBACKT_HRESHOLD_VALUE    = PropertyFactory.getLong(SNOWFLAKE_BLOCKBACKT_HRESHOLD);
    //public static final String SNOWFLAKE_KEEPALIVE_INTERVAL         = "bx.snowflake.keepAliveInterval";
    //public static final long SNOWFLAKE_KEEPALIVE_INTERVAL_VALUE     = PropertyFactory.getLong(SNOWFLAKE_KEEPALIVE_INTERVAL);
    //public static final String SNOWFLAKE_SYSTEM_REPORT_INTERVAL     = "bx.snowflake.systemReportInterval";
    //public static final long SNOWFLAKE_SYSTEM_REPORT_INTERVAL_VALUE = PropertyFactory.getLong(SNOWFLAKE_SYSTEM_REPORT_INTERVAL);
    //public static final String SNOWFLAKE_LEASE_TTL                  = "bx.snowflake.leaseTTL";
    //public static final long SNOWFLAKE_LEASE_TTL_VALUE              = PropertyFactory.getLong(SNOWFLAKE_LEASE_TTL);


//    public static final String THREAD_LOCAL_FETCH_SIZE = "threadLocalFetchSize";
//    public static final int THREAD_LOCAL_FETCH_SIZE_VALUE = PropertyFactory.getInt(THREAD_LOCAL_FETCH_SIZE);
//
//    public static final String SEGMENT_FETCH_SIZE = "segmentFetchSize";
//    public static final int SEGMENT_FETCH_SIZE_VALUE = PropertyFactory.getInt(SEGMENT_FETCH_SIZE);
//
//    public static final String NEXT_SEGMENT_FETCH_PERCENT = "nextSegmentFetchPercent";
//    public static final BigDecimal NEXT_SEGMENT_FETCH_PERCENT_VALUE = PropertyFactory.getBigDecimal(NEXT_SEGMENT_FETCH_PERCENT);
//
//    public static final String THREAD_LOCAL_CACHE_ENABLED = "threadLocalCacheEnabled";
//    public static final boolean THREAD_LOCAL_CACHE_ENABLED_VALUE = PropertyFactory.getBoolean(THREAD_LOCAL_CACHE_ENABLED);


    public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;
    /**
     * 2008-08-08 00:00:00
     */
    public static final Long SNOWFLAKE_DEFAULT_TWEPOCH = 1218124800000L;


    public static final String SEGEMNT_FLAG = "_sg_";
    public static final String LOCK_PRE = "/lock/";
    public static final String ID_PRE = "/id/";
    public static final String SERVER_TIME_API = "/generator/id/api/time";
    public static final String SPLIT_CHAR = "@";


    /**
     * 获得操作ETCD节点的组装key
     *
     * @param key 业务key
     * @return
     */
    public static String getETCDKeyBySystemId(String key) {
        String lockKey = ID_PRE + key;
        return lockKey;
    }

    /**
     * 根据系统ID 和 tag 获得key  例如 123_sg_oms
     *
     * @param keyInfoBean
     * @return
     */
    public static String getKeyBySystemIdAndTag(KeyInfoBean keyInfoBean) {
        String systemId = keyInfoBean.getSystemId();
        String key = systemId + SEGEMNT_FLAG + keyInfoBean.getBizTag();
        return key;
    }

    public static String getKeyBySysIdAndTagNew(IdGenerateBean bean) {
        String key = bean.getSystemId() + SPLIT_CHAR + bean.getBizTag();
        return key;
    }

    /**
     * 根据组id  获得key 例如:123_sg_
     *
     * @param dto
     * @return
     */
    public static String getKeyBySystemId(KeyInfoBean dto) {
        String uId = dto.getSystemId();
        String key = uId + SEGEMNT_FLAG;
        return key;
    }

    /**
     * 获得锁的key
     *
     * @param key 业务组装key
     * @return
     */
    public static String getLockKeyByKey(String key) {
        String lockKey = LOCK_PRE + key;
        return lockKey;

    }
}
