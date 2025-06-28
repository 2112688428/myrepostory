package org.bx.idgenerator.base.getter.snowflake.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 服务节点 上报 ETCD的数据，即存储在 ETCD 的 K-V节点中的 Value
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月07日 00时38分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
@AllArgsConstructor
public class EndPointReportData {
    /** 机器IP地址 */
    private String ip;
    /** 端口地址 */
    private String port;
    /** 当前时间戳 */
    private long timestamp;
    /** 当前worker节点id */
    private long workerId;
}
