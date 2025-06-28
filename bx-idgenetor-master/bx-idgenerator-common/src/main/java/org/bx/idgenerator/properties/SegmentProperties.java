package org.bx.idgenerator.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "bx.id-generator.segment")
public class SegmentProperties {

    private BigDecimal nextSegmentFetchPercent;
    private Integer segmentFetchSize;
    private Integer threadLocalFetchSize;
    private Boolean threadLocalCacheEnabled;

}
