package org.bx.idgenerator.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.msb.cube.common.base.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.bean.IdGeneratorGroupBean;
import org.bx.idgenerator.bean.LoginUserBean;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.exception.BizException;
import org.bx.idgenerator.manager.CurrentContextHolder;
import org.bx.idgenerator.manager.db.dao.IdGeneratorGroupMapper;
import org.bx.idgenerator.manager.entity.IdGeneratorGroup;
import org.bx.idgenerator.manager.service.IIdGeneratorGroupService;
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
 * @date 2020-07-08
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Service
@Slf4j
public class IdGeneratorGroupServiceImpl extends ServiceImpl<IdGeneratorGroupMapper, IdGeneratorGroup> implements IIdGeneratorGroupService {

    @Autowired
    private IdGeneratorGroupMapper groupMapper;

    @Override
    public ResultWrapper<PageResult<IdGeneratorGroupBean>> listGroup(Integer pageNum, Integer pageSize) {
        LoginUserBean user = CurrentContextHolder.getUser();
        Integer groupId = user.getGroupId();
        Page<IdGeneratorGroup> page = new Page<>(pageNum,pageSize);
        if (groupId == 1){
            // 管理员查看全部
            Wrapper<IdGeneratorGroup> wrapperAll = new QueryWrapper<IdGeneratorGroup>().lambda();
            this.page(page,wrapperAll);
        }else {
            // 非管理员查看自己的
            Wrapper<IdGeneratorGroup> wrapperByGroupId = new QueryWrapper<IdGeneratorGroup>().lambda().eq(IdGeneratorGroup::getId, groupId);
            this.page(page,wrapperByGroupId);
        }

        List<IdGeneratorGroupBean> groupList = Lists.newArrayList();
        PageResult<IdGeneratorGroupBean> pageResult = new PageResult<>();
        pageResult.setTotalElements(page.getTotal());
        pageResult.setNumber(page.getCurrent());
        pageResult.setTotalPages(page.getPages());
        BeanCopier copier = BeanCopier.create(IdGeneratorGroup.class, IdGeneratorGroupBean.class, false);
        if (page.getRecords() == null || page.getRecords().isEmpty()){
            return ResultWrapper.getSuccessResultWrapper(null);
        }
        page.getRecords().stream().forEach(group -> {
            IdGeneratorGroupBean groupBean = new IdGeneratorGroupBean();
            copier.copy(group,groupBean,null);
            groupList.add(groupBean);
        });

        pageResult.setContent(groupList);
        return ResultWrapper.getSuccessResultWrapper(pageResult);


    }

    @Override
    public ResultWrapper<Integer> addGroup(IdGeneratorGroupBean groupBean) {
        log.info("新增组  入参:{}",new Gson().toJson(groupBean));

        IdGeneratorGroup group = new IdGeneratorGroup();
        BeanCopier copier = BeanCopier.create(IdGeneratorGroupBean.class, IdGeneratorGroup.class, false);
        copier.copy(groupBean,group,null);
        group.setEnable(true);
        group.setCreateTime(LocalDateTime.now());
        int insertGroup = groupMapper.insert(group);
        if (insertGroup <= 0){
            throw new BizException("新增组异常");
        }
        log.info("新增组入库 insertGroup:{}",insertGroup);
        return ResultWrapper.getSuccessResultWrapper(null);
    }

    @Override
    public ResultWrapper<Integer> deleteGroup(Integer id) {
        log.info("删除组  入参:{}",id);

        IdGeneratorGroup group = new IdGeneratorGroup();
        group.setId(id);
        group.setEnable(false);
        group.setUpdateTime(LocalDateTime.now());
        int deleteGroup = groupMapper.updateById(group);
        if (deleteGroup <= 0){
            throw new BizException("删除组异常");
        }
        log.info("删除组 deleteGroup:{}",deleteGroup);
        return ResultWrapper.getSuccessResultWrapper(null);
    }

    @Override
    public ResultWrapper<Integer> updateGroup(IdGeneratorGroupBean groupBean) {
        log.info("更新组  入参:{}",new Gson().toJson(groupBean));

        IdGeneratorGroup group = new IdGeneratorGroup();
        BeanCopier copier = BeanCopier.create(IdGeneratorGroupBean.class, IdGeneratorGroup.class, false);
        copier.copy(groupBean,group,null);
        group.setUpdateTime(LocalDateTime.now());
        int updateGroup = groupMapper.updateById(group);
        if (updateGroup <= 0){
            throw new BizException("更新组异常");
        }
        log.info("更新组 updateGroup:{}",updateGroup);
        return ResultWrapper.getSuccessResultWrapper(null);

    }

    @Override
    public ResultWrapper<IdGeneratorGroupBean> groupDetail(Integer id) {
        log.info("查询组信息  入参:{}",id);

        IdGeneratorGroupBean groupBean = new IdGeneratorGroupBean();
        IdGeneratorGroup group = groupMapper.selectById(id);
        if (group == null){
            throw new BizException("查询组异常");
        }
        BeanCopier copier = BeanCopier.create(IdGeneratorGroup.class, IdGeneratorGroupBean.class, false);
        copier.copy(group,groupBean,null);
        log.info("查询组 group:{}",new Gson().toJson(group));
        return ResultWrapper.getSuccessResultWrapper(null);

    }
}
