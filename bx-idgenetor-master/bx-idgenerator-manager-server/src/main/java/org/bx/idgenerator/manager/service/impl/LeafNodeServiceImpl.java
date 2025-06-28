package org.bx.idgenerator.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.cube.common.base.dto.PageResult;
import com.msb.cube.common.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.exception.BizException;
import org.bx.idgenerator.manager.db.dao.LeafNodeMapper;
import org.bx.idgenerator.manager.dto.LeafNodeDTO;
import org.bx.idgenerator.manager.dto.LeafNodePageDTO;
import org.bx.idgenerator.manager.entity.LeafNode;
import org.bx.idgenerator.manager.service.ILeafNodeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
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
public class LeafNodeServiceImpl extends ServiceImpl<LeafNodeMapper, LeafNode>
        implements ILeafNodeService {


    @Override
    public PageResult<LeafNode> pageList(LeafNodePageDTO leafNodePageDTO) {
        Integer length = leafNodePageDTO.getLength();
        Integer pageIndex = leafNodePageDTO.getPageIndex();
        Page<LeafNode> page = new Page<>(pageIndex, length);

        LambdaQueryWrapper<LeafNode> lambda = handleQueryParam(leafNodePageDTO);
        lambda.orderByAsc(LeafNode::getId);
        this.page(page, lambda);
        PageResult<LeafNode> pageResult = new PageResult<>();
        pageResult.setContent(page.getRecords());
        pageResult.setTotalElements(page.getTotal());
        pageResult.setTotalPages(page.getPages());
        pageResult.setNumber(page.getCurrent());
        return pageResult;
    }


    private LambdaQueryWrapper<LeafNode> handleQueryParam(LeafNodePageDTO leafNodePageDTO) {
        LambdaQueryWrapper<LeafNode> lambda = new QueryWrapper<LeafNode>().lambda();
        Integer groupId = leafNodePageDTO.getGroupId();
        if (groupId != null) {
            lambda.eq(LeafNode::getGroupId, groupId);
        }
        String systemId = leafNodePageDTO.getSystemId();
        if (!StringUtils.isEmpty(systemId)) {
            lambda.likeRight(LeafNode::getSystemId, systemId);
        }
        String bizTag = leafNodePageDTO.getBizTag();
        if (!StringUtils.isEmpty(bizTag)) {
            lambda.likeRight(LeafNode::getBizTag, bizTag);
        }
        Boolean enableFlag = leafNodePageDTO.getEnableFlag();
        if (enableFlag != null) {
            lambda.eq(LeafNode::getEnableFlag, enableFlag);
        }
        return lambda;
    }

    @Override
    public List<LeafNode> listByCurrent() {
        return this.list();
    }

    @Override
    public ResultWrapper<String> add(LeafNodeDTO leafNodeDTO) {
        log.info("LeafNodeServiceImpl.add 入参:{}", leafNodeDTO);
        if (leafNodeDTO.getGroupId() == null
                || StringUtils.isBlank(leafNodeDTO.getBizTag())
                || StringUtils.isBlank(leafNodeDTO.getSystemId())
                || leafNodeDTO.getStep() == null
                || leafNodeDTO.getStep() <= 0
        ) {
            throw new BizException("新增节点的时候groupId和bizTag不能为null");
        }
        if (findBySystemIDAndBizTag(leafNodeDTO.getSystemId(), leafNodeDTO.getBizTag()).getData() != null) {
            throw new BizException("已经存在节点");
        }
        Long maxId = leafNodeDTO.getMaxId();
        if (maxId == null || maxId == 0L) {
            leafNodeDTO.setMaxId(1L);
        }

        LeafNode leafNode = BeanUtils.copyBean(leafNodeDTO, LeafNode.class);
        leafNode.setUpdateTime(System.currentTimeMillis());
        boolean save = this.save(leafNode);

        return ResultWrapper.getSuccessResultWrapper(save ? "新增成功" : "新增失败");
    }

    @Override
    public ResultWrapper<String> doDelete(Integer id) {
        log.info("LeafNodeServiceImpl.doDelete 入参:{}", id);
        LambdaQueryWrapper<LeafNode> lambda = new QueryWrapper<LeafNode>().lambda();
        lambda.eq(LeafNode::getId, id)
                .eq(LeafNode::getEnableFlag, false);
        int size = this.baseMapper.delete(lambda);
        if (size != 1) {
            log.error("删除节点失败:{}", id);
            throw new BizException("删除失败,只能删除已经被禁用的节点");
        }
        log.info("删除节点成功:{}", id);
        return ResultWrapper.SUCCESS_BUILDER().data("删除成功").build();
    }

    @Override
    public ResultWrapper<String> doDeactivate(Integer id) {
        log.info("LeafNodeServiceImpl.doDeactivate 入参:{}", id);
        updateEnableFlag(id, false);
        return ResultWrapper.SUCCESS_BUILDER().data("禁用节点成功").build();
    }

    private void updateEnableFlag(Integer id, boolean enableFlag) {

        LambdaUpdateWrapper<LeafNode> lambda = new UpdateWrapper<LeafNode>().lambda();
        lambda.eq(LeafNode::getId, id)
                .eq(LeafNode::getEnableFlag, !enableFlag)
                .set(LeafNode::getEnableFlag, enableFlag)
                .set(LeafNode::getUpdateTime, System.currentTimeMillis());
        int size = this.baseMapper.update(null, lambda);
        if (size != 1) {
            log.error("更新节点enableFlag失败:{},pre:{},curr:{}", id, !enableFlag, enableFlag);
            throw new BizException("更新节点enableFlag失败");
        }
        log.info("更新节点enableFlag失败:{},pre:{},curr:{}", id, !enableFlag, enableFlag);
    }

    @Override
    public ResultWrapper<String> doEnabled(Integer id) {
        log.info("LeafNodeServiceImpl.doEnabled 入参:{}", id);
        updateEnableFlag(id, true);
        return ResultWrapper.SUCCESS_BUILDER().data("启用节点成功").build();
    }

    @Override
    public ResultWrapper<String> doUpdateByManager(LeafNodeDTO leafNode) {
        log.info("LeafNodeServiceImpl.doUpdate 入参:{}", leafNode);
        if (leafNode.getGroupId() == null
                || StringUtils.isBlank(leafNode.getBizTag())
                || StringUtils.isBlank(leafNode.getSystemId())) {
            throw new BizException("更新节点的时候groupId和bizTag不能为null");
        }
        QueryWrapper<LeafNode> queryWrapper = getQueryWrapperBySystemIDAndBizTag(leafNode.getSystemId(), leafNode.getBizTag());
        LeafNode node = findBySystemIDAndBizTag(leafNode.getSystemId(), leafNode.getBizTag()).getData();
        if (node == null) {
            return ResultWrapper.ERROR_BUILDER().data("节点不存在").build();
        }
        node.setDescription(leafNode.getDescription());
        node.setGroupId(leafNode.getGroupId());
        node.setUpdateTime(System.currentTimeMillis());
        this.update(node, queryWrapper);
        return ResultWrapper.SUCCESS_BUILDER().data("更新成功").build();

    }

    @Override
    public ResultWrapper<LeafNode> detail(Long id) {
        LeafNode node = this.getById(id);
        return ResultWrapper.SUCCESS_BUILDER().data(node).build();
    }

    @Override
    public ResultWrapper<LeafNode> findBySystemIDAndBizTag(String systemId, String bizTag) {
        QueryWrapper<LeafNode> queryWrapper = getQueryWrapperBySystemIDAndBizTag(systemId, bizTag);
        LeafNode node = this.getOne(queryWrapper);
        return ResultWrapper.<LeafNode>builder().data(node).build();
    }

    private QueryWrapper<LeafNode> getQueryWrapperBySystemIDAndBizTag(String systemId, String bizTag) {
        QueryWrapper<LeafNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(LeafNode::getBizTag, bizTag).eq(LeafNode::getSystemId, systemId);
        return queryWrapper;
    }


}

