package org.bx.idgenerator.base.segment;

import org.bx.idgenerator.base.segment.entity.LeafInfo;

/**
 * 获取leaf节点信息，对segment获取leaf info 屏蔽
 */
public interface ILeafInfoService {
    /**
     * 获取leaf info
     *
     * @param tag 业务标识  123_sg_oms
     * @return
     */
    LeafInfo getLeafInfo(String tag);
}
