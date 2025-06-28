package org.bx.idgenerator.bean;

import lombok.Data;

/**
 * 雪花算法交互类
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月14日 12时53分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
public class SnowFlakeEndPointDTO {
    /** 机器IP地址 */
    private String ip;
    /** 端口地址 */
    private String port;
    /** 当前时间戳 */
    private long timestamp;
    /** 机器节点ID */
    private long workerId;
    /** ETCD 节点路径 */
    private String etcdNodePath;
    /** 是否在线服务 */
    private Boolean onLineFlag = false;
}
