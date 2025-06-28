package org.bx.idgenerator.base.getter.segment;

import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.base.segment.ILeafInfoService;
import org.bx.idgenerator.base.segment.CacheSegmentIDGenerator;
import org.bx.idgenerator.base.segment.bus.FillSegmentUpdater;
import org.bx.idgenerator.base.segment.policy.IFetchPolicy;
import org.bx.idgenerator.core.server.IdGetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 14:41
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@ConditionalOnProperty(prefix = "bx.idgenerator",name = "segment",havingValue = "true")
//@ConditionalOnExpression("${bx.idgenerator.segment}")
@Service("segmentServiceImpl")
@Slf4j
public class SegmentServiceImpl implements IdGetterService {

    /**
     * etcd操作 双buffer用
     */
    @Autowired
    @Qualifier("mysqlLeafInfoService")
    private ILeafInfoService leafInfoService;

    /**
     * 填充消息-驱动
     */
    @Autowired
    private FillSegmentUpdater fillSegmentUpdater;
    /**
     * 动态策略
     */
    @Autowired
    private IFetchPolicy iFetchPolicy;

    /**
     * id获取业务类的实例
     */
    private CacheSegmentIDGenerator cacheSegmentIdGenerator;

    @PostConstruct
    public void init() {
        log.info("Init  SegmentServiceImpl ...");
        cacheSegmentIdGenerator = new CacheSegmentIDGenerator(leafInfoService, fillSegmentUpdater, iFetchPolicy);
    }

    @Override
    public long getId(String key) {
        return cacheSegmentIdGenerator.genId(key);
    }

}
