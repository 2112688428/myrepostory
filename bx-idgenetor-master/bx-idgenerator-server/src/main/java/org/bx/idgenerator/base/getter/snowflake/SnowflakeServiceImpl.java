package org.bx.idgenerator.base.getter.snowflake;

import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.core.server.IdGetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 14:42
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@ConditionalOnProperty(prefix = "bx.idgenerator", name = "snowflake", havingValue = "true")
@Service("snowflakeServiceImpl")
@Slf4j
public class SnowflakeServiceImpl implements IdGetterService {

    @Autowired
    private SnowFlakeIdGenerator snowFlakeIdGenerator;

    @Override
    public long getId(String key) {
        return snowFlakeIdGenerator.genId(key);
    }

}
