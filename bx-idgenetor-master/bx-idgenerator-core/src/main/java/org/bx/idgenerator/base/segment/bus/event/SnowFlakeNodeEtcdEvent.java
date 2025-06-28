package org.bx.idgenerator.base.segment.bus.event;

import lombok.Data;
import org.bx.idgenerator.bean.NodeTypeEnum;
import org.bx.idgenerator.base.segment.bus.Event;
import org.bx.idgenerator.base.segment.bus.eventenum.OperateNodeEnum;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * 雪花算法操作ETCD事件
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020年07月06日 11时19分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
public class SnowFlakeNodeEtcdEvent implements Event {

    private DeferredResult deferredResult;

    private NodeTypeEnum nodeTypeEnum;

    private OperateNodeEnum operateNodeEnum;

    @Override
    public String getIdentity() {
        return null;
    }

}
