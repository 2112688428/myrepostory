package org.bx.idgenerator.server.config;

import com.google.common.base.Preconditions;
import io.etcd.jetcd.Client;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.properties.SnowFlakeProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * ETCD客户端工厂
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月06日 16时05分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Slf4j
@Configuration
@Data
public class EtcdClientFactory {

    public static final String SEPARATOR_TAG = ",";
    @Value("${bx.id-generator.snowflake.etcdPoints}")
    private String etcdPoints;

    @Bean
    public Client etcdClient() {
        List<URI> endPoints = new ArrayList<>();
        Preconditions.checkArgument(etcdPoints != null && !etcdPoints.isEmpty(), "etcd's endpoints must not be null");
        String[] array = etcdPoints.split(SEPARATOR_TAG);
        for (String endPoint : array) {
            URI uri = URI.create(endPoint);
            endPoints.add(uri);
        }
        return Client.builder().endpoints(endPoints).build();
    }


}
