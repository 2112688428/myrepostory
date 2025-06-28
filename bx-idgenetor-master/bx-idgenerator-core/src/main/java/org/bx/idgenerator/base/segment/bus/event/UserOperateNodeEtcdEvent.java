package org.bx.idgenerator.base.segment.bus.event;

import lombok.Data;
import org.bx.idgenerator.bean.KeyInfoBean;
import org.bx.idgenerator.base.segment.bus.Event;
import org.bx.idgenerator.base.segment.bus.eventenum.OperateNodeEnum;
import org.springframework.web.context.request.async.DeferredResult;

@Data
public class UserOperateNodeEtcdEvent implements Event {

    private DeferredResult deferredResult;

    private KeyInfoBean keyInfoBean;

    private OperateNodeEnum operateNodeEnum;

    @Override
    public String getIdentity() {
        return null;
    }



}
