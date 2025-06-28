package org.bx.idgenerator;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020/7/8/17:00
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "org.bx.idgenerator")
@MapperScan(value = "org.bx.idgenerator.base.mysql.db.dao")
@Slf4j
public class IdGeneratorServerApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application =
                SpringApplication.run(IdGeneratorServerApplication.class, args);

        Environment env = application.getEnvironment();
        String contextPath = env.getProperty("server.servlet.context-path");
        String port = env.getProperty("server.port");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "Doc: \thttp://{}:{}{}/doc.html\n" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                port,
                InetAddress.getLocalHost().getHostAddress(),
                port,
                InetAddress.getLocalHost().getHostAddress(),
                port,
                contextPath == null?"":contextPath);
    }
}
