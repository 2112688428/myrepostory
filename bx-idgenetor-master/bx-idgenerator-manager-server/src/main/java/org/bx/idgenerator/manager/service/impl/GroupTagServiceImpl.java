package org.bx.idgenerator.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.msb.cube.common.base.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.bean.GroupTagBean;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.exception.BizException;
import org.bx.idgenerator.manager.db.dao.GroupTagMapper;
import org.bx.idgenerator.manager.entity.GroupTag;
import org.bx.idgenerator.manager.service.IGroupTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact
 * @date 2020-07-09
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Service
@Slf4j
public class GroupTagServiceImpl extends ServiceImpl<GroupTagMapper, GroupTag> implements IGroupTagService {

    @Autowired
    private GroupTagMapper tagMapper;

    @Override
    public ResultWrapper<PageResult<GroupTagBean>> pageListTag(Integer groupId, Integer pageNum, Integer pageSize) {

        Page<GroupTag> page = new Page<>(pageNum,pageSize);
        if (groupId == 1){
            // 管理员查看全部
            Wrapper<GroupTag> wrapperAll = new QueryWrapper<GroupTag>().lambda().eq(GroupTag::getEnabled,true);
            this.page(page,wrapperAll);
        }else {
            // 查看自己组的
            Wrapper<GroupTag> wrapperByGroupId = new QueryWrapper<GroupTag>().lambda().eq(GroupTag::getGroupId, groupId);
            this.page(page,wrapperByGroupId);
        }

        List<GroupTagBean> tagList = Lists.newArrayList();
        BeanCopier copier = BeanCopier.create(GroupTag.class, GroupTagBean.class, false);
        if (page.getRecords() == null || page.getRecords().isEmpty()){
            return ResultWrapper.getSuccessResultWrapper(null);
        }
        page.getRecords().stream().forEach(tag -> {
            GroupTagBean bean = new GroupTagBean();
            copier.copy(tag,bean,null);
            tagList.add(bean);
        });

        PageResult<GroupTagBean> pageResult = new PageResult<>();
        pageResult.setContent(tagList);
        pageResult.setTotalElements(page.getTotal());
        return ResultWrapper.getSuccessResultWrapper(pageResult);

    }

    @Override
    public ResultWrapper<Integer> addTag(GroupTagBean tagBean) {
        log.info("新增节点地址  入参:{}",new Gson().toJson(tagBean));
        GroupTag tag = new GroupTag();
        BeanCopier copier = BeanCopier.create(GroupTagBean.class, GroupTag.class, false);
        copier.copy(tagBean,tag,null);
        tag.setEnabled(true);
        tag.setCreateTime(LocalDateTime.now());
        int insertTag = tagMapper.insert(tag);
        if (insertTag <= 0){
            throw new BizException("新增节点异常");
        }
        log.info("新增节点地址入库 insertTag:{}",insertTag);
        return ResultWrapper.getSuccessResultWrapper(null);
    }

    @Override
    public ResultWrapper<Integer> deleteTag(Integer id) {
        log.info("删除节点地址  入参:{}",id);

        GroupTag tag = new GroupTag();
        tag.setEnabled(false);
        tag.setId(id);
        tag.setCreateTime(LocalDateTime.now());
        int deleteTag = tagMapper.updateById(tag);
        if (deleteTag <= 0){
            throw new BizException("删除节点地址异常");
        }
        log.info("删除节点地址 deleteTag:{}",deleteTag);
        return ResultWrapper.getSuccessResultWrapper(null);
    }

    @Override
    public ResultWrapper<Integer> updateTag(GroupTagBean tagVO) {
        log.info("更新节点地址  入参:{}",new Gson().toJson(tagVO));

        GroupTag tag = new GroupTag();
        BeanCopier copier = BeanCopier.create(GroupTagBean.class, GroupTag.class, false);
        copier.copy(tagVO,tag,null);
        tag.setUpdateTime(LocalDateTime.now());
        int updateTag = tagMapper.updateById(tag);
        if (updateTag <= 0){
            throw new BizException("更新节点地址异常");
        }
        log.info("更新节点地址 updateTag:{}",updateTag);
        return ResultWrapper.getSuccessResultWrapper(null);
    }

    @Override
    public ResultWrapper<GroupTagBean> tagDetail(Integer id) {
        log.info("查询节点地址  入参:{}",id);

        GroupTagBean tagBean = new GroupTagBean();
        GroupTag tag = tagMapper.selectById(id);
        if (tag == null){
            throw new BizException("查询节点地址异常");
        }
        BeanCopier copier = BeanCopier.create(GroupTag.class, GroupTagBean.class, false);
        copier.copy(tag,tagBean,null);
        log.info("查询节点地址 tag:{}",new Gson().toJson(tagBean));
        return ResultWrapper.getSuccessResultWrapper(null);
    }
}
