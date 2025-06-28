package org.bx.idgenerator.server.config;

import org.bx.idgenerator.base.segment.bus.FillSegmentUpdater;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020/12/2 14:09
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Configuration
public class IdServerConfig {

    @Bean
    public FillSegmentUpdater fillSegmentUpdater() {
        FillSegmentUpdater fillSegmentUpdater = new FillSegmentUpdater();
        return fillSegmentUpdater;
    }

}
