package org.bx.idgenerator.manager;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月13日 22时26分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@ConfigurationProperties(prefix = "jwt")
@Configuration
@Data
public class JwtProperty {
    private String tokenHeader;
    private Long expiration;
    private String secret;
    private String tokenHead;
}
