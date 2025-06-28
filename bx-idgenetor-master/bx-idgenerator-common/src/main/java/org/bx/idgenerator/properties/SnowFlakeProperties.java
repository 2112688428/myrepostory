package org.bx.idgenerator.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "bx.id-generator.snowflake")
public class SnowFlakeProperties {

    private String workerIp;
    private String workerPort;
    private String generatorName;
    private Long blockBackThreshold;
    private String etcdPoints;
    private Long keepAliveInterval;
    private Long leaseTtl;
    private Long systemReportInterval;

    /**
     * ETCD 节点根节点
     */
    private String rootEtcdPath;
    /**
     * 保存所有服务器节点数据的持久节点路径
     */
    private String pathForever;
    /**
     * 保持服务器节点信息的临时节点路径
     */
    private String pathTemp;
    /**
     * 指定获取启动服务IP的的网卡
     */
    private String netInterfaceName;

}
