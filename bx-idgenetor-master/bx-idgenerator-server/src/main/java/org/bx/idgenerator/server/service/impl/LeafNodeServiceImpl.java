package org.bx.idgenerator.server.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.base.segment.entity.LeafNode;
import org.bx.idgenerator.base.mysql.db.dao.LeafNodeMapper;
import org.bx.idgenerator.core.server.ILeafNodeService;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact
 * @date 2020-10-22
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Slf4j
@Service
public class LeafNodeServiceImpl extends ServiceImpl<LeafNodeMapper, LeafNode> implements ILeafNodeService {


    @Override
    public ResultWrapper<LeafNode> findBySystemIDAndBizTag(String systemId, String bizTag) {
        QueryWrapper<LeafNode> queryWrapper = getQueryWrapperBySystemIDAndBizTag(systemId, bizTag);
        LeafNode node = this.getOne(queryWrapper);
        return ResultWrapper.<LeafNode>builder().data(node).build();
    }

    private QueryWrapper<LeafNode> getQueryWrapperBySystemIDAndBizTag(String systemId, String bizTag){
        QueryWrapper<LeafNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(LeafNode::getBizTag, bizTag).eq(LeafNode::getSystemId, systemId);
        return queryWrapper;
    }


}

