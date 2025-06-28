package org.bx.idgenerator.manager.service;

import org.bx.idgenerator.bean.*;

import java.util.List;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public interface IEtcdNodeOperateService {
    /**
     * 新增节点
     * @param dto
     * @return
     */
    ResultWrapper<String> add(EtcdKeyInfoBean dto);

    /**
     * 更新节点信息
     * @param dto
     * @return
     */
    ResultWrapper<String> update(EtcdKeyInfoBean dto);

    /**
     * 禁用节点
     * @param dto
     * @return
     */
    ResultWrapper<String> delete(EtcdKeyInfoBean dto);

    /**
     * 根据用户组id 查询所有 有效 节点
     * @param dto
     * @return
     */
    ResultWrapper<List<KeyInfoBean>> select(EtcdKeyInfoBean dto);

    /**
     * 查询所有的SnowFlake下的节点信息
     * @param nodeTypeEnum
     * @return
     */
    ResultWrapper<List<SnowFlakeEndPointDTO>> selectSnowFlakeEndPoint(NodeTypeEnum nodeTypeEnum);
}

