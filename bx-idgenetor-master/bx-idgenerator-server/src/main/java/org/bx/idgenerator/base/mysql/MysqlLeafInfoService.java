package org.bx.idgenerator.base.mysql;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.Constants;
import org.bx.idgenerator.base.segment.ILeafInfoService;
import org.bx.idgenerator.base.segment.entity.LeafInfo;
import org.bx.idgenerator.base.segment.entity.LeafNode;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.core.server.ILeafNodeService;
import org.bx.idgenerator.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分配并获取节点信息
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 15:55
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Slf4j
@Service("mysqlLeafInfoService")
public class MysqlLeafInfoService implements ILeafInfoService {


    @Autowired
    private ILeafNodeService leafNodeService;
    /**
     * 最大尝试次数
     */
    private static final int MAX_TRIES = 3;

    @Override
    public LeafInfo getLeafInfo(String tag) {


        String[] split = tag.split(Constants.SPLIT_CHAR);
        String sysId = split[0];
        String bizTag = split[1];
        ResultWrapper<LeafNode> detail = leafNodeService.findBySystemIDAndBizTag(sysId, bizTag);

        LeafNode leafNode = detail.getData();
        if (leafNode == null) {
            throw new BizException("不存在此tag:{" + tag + "}的数据");
        }
        Boolean enableFlag = leafNode.getEnableFlag();
        if (enableFlag == null || !enableFlag) {
            throw new BizException("此tag:{" + tag + "}的数据已经被禁用");
        }
        log.info("tag:{},info:{}", tag, detail);
        //步长
        Integer step = leafNode.getStep();
        //旧的最大id
        Long maxId = leafNode.getMaxId();

        //新的最大id
        Long newMaxId = step + maxId;

        //设置新的最大id
        leafNode.setMaxId(newMaxId);
        //更新时间
        leafNode.setUpdateTime(System.currentTimeMillis());
        //乐观更新
        LambdaUpdateWrapper<LeafNode> eq = new UpdateWrapper<LeafNode>().lambda()
                .eq(LeafNode::getSystemId, sysId)
                .eq(LeafNode::getBizTag, bizTag)
                .eq(LeafNode::getMaxId, maxId);
        // TODO 重试更新
        int i = 0;
        boolean update = false;
        while (i++ < MAX_TRIES && !update) {
            update = leafNodeService.update(leafNode, eq);
        }

        if (!update) {
            throw new BizException("更新失败");
        }

        //返回
        LeafInfo leafInfo = new LeafInfo();
        leafInfo.setTag(bizTag);
        leafInfo.setCurId(maxId);
        leafInfo.setMaxId(newMaxId);
        leafInfo.setDescription(leafNode.getDescription());
        leafInfo.setUpdateTime(leafNode.getUpdateTime());
        return leafInfo;
    }
}
