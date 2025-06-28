package org.bx.idgenerator.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.msb.cube.common.base.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.bean.IdGeneratorUserBean;
import org.bx.idgenerator.bean.LoginUserBean;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.exception.BizException;
import org.bx.idgenerator.manager.CurrentContextHolder;
import org.bx.idgenerator.manager.JwtTokenUtil;
import org.bx.idgenerator.manager.db.dao.IdGeneratorGroupMapper;
import org.bx.idgenerator.manager.db.dao.IdGeneratorUserMapper;
import org.bx.idgenerator.manager.entity.IdGeneratorGroup;
import org.bx.idgenerator.manager.entity.IdGeneratorUser;
import org.bx.idgenerator.manager.service.IIdGeneratorUserService;
import org.bx.idgenerator.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
public class IdGeneratorUserServiceImpl extends ServiceImpl<IdGeneratorUserMapper, IdGeneratorUser> implements IIdGeneratorUserService {

    @Autowired
    private IdGeneratorUserMapper userMapper;

    @Autowired
    private IdGeneratorGroupMapper groupMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public ResultWrapper<PageResult<IdGeneratorUserBean>> pageListUser(Integer pageNum, Integer pageSize) {
        //获取当前登录用户信息
        LoginUserBean user = CurrentContextHolder.getUser();
        Integer groupId = user.getGroupId();

        Page<IdGeneratorUser> page = new Page<>(pageNum,pageSize);
        if (groupId == 1){
            // 管理员查看全部
            Wrapper<IdGeneratorUser> wrapperAll = new QueryWrapper<IdGeneratorUser>().lambda();
            this.page(page,wrapperAll);
        }else {
            // 查看自己组的
            Wrapper<IdGeneratorUser> wrapperByGroupId = new QueryWrapper<IdGeneratorUser>().lambda().eq(IdGeneratorUser::getGroupId, groupId);
            this.page(page,wrapperByGroupId);
        }

        List<IdGeneratorUserBean> userList = Lists.newArrayList();
        BeanCopier copier = BeanCopier.create(IdGeneratorUser.class, IdGeneratorUserBean.class, false);
        if (page.getRecords() == null || page.getRecords().isEmpty()){
            return ResultWrapper.getSuccessResultWrapper(null);
        }
        page.getRecords().stream().forEach(generatorUser -> {
            IdGeneratorUserBean userBean = new IdGeneratorUserBean();
            copier.copy(generatorUser,userBean,null);

            IdGeneratorGroup group = groupMapper.selectById(generatorUser.getGroupId());
            userBean.setGroupName(group.getGroupName());
            userList.add(userBean);
        });

        PageResult<IdGeneratorUserBean> pageResult = new PageResult<>();
        pageResult.setContent(userList);
        pageResult.setTotalElements(page.getTotal());
        pageResult.setNumber(page.getCurrent());
        pageResult.setTotalPages(page.getPages());
        return ResultWrapper.getSuccessResultWrapper(pageResult);
    }

    @Override
    public ResultWrapper<LoginUserBean> loginUser(String username, String password, HttpServletRequest request) {
        String pwd = MD5Utils.md5(password);

        LoginUserBean user = userMapper.login(username, pwd);
        if (user == null){
            throw new BizException("登录失败!请检查账号和密码是否正确");
        }
        if(!user.getEnabled()){
            throw new BizException("该账号被禁用,暂时无法登录.请联系管理员了解详细信息");
        }
        String token = jwtTokenUtil.generateTokenWithHead(user);
        user.setToken(token);
        return ResultWrapper.getSuccessResultWrapper(user);
    }

    @Override
    public ResultWrapper<Integer> addUser(IdGeneratorUserBean userBean) {
        log.info("新增用户  入参:{}",new Gson().toJson(userBean));

        Wrapper<IdGeneratorUser> wrapper = new QueryWrapper<IdGeneratorUser>().lambda().eq(IdGeneratorUser::getUsername,userBean.getUsername());
        IdGeneratorUser oldUser = userMapper.selectOne(wrapper);
        if (oldUser != null){
            throw new BizException("用户名已存在");
        }
        IdGeneratorUser user = new IdGeneratorUser();
        BeanCopier copier = BeanCopier.create(IdGeneratorUserBean.class, IdGeneratorUser.class, false);
        copier.copy(userBean,user,null);
        user.setEnabled(true);
        user.setCreateTime(LocalDateTime.now());
        user.setPassword(MD5Utils.md5(userBean.getPassword()));
        int insertUser = userMapper.insert(user);
        if (insertUser <= 0){
            throw new BizException("新增用户异常");
        }
        log.info("新增用户入库 insertUser:{}",insertUser);
        return ResultWrapper.getSuccessResultWrapper(null);
    }

    @Override
    public ResultWrapper<Integer> deleteUser(Integer id) {
        log.info("删除用户  入参:{}",id);
        IdGeneratorUser user = new IdGeneratorUser();
        user.setId(id);
        user.setEnabled(false);
        user.setUpdateTime(LocalDateTime.now());
        int deleteUser = userMapper.updateById(user);
        if (deleteUser <= 0){
            throw new BizException("禁用账号失败");
        }
        log.info("删除用户 deleteUser:{}",deleteUser);
        return ResultWrapper.getSuccessResultWrapper(null);
    }

    @Override
    public ResultWrapper<Integer> updateUser(IdGeneratorUserBean userBean) {
        log.info("更新用户  入参:{}",new Gson().toJson(userBean));
        IdGeneratorUser user = new IdGeneratorUser();
        BeanCopier copier = BeanCopier.create(IdGeneratorUserBean.class, IdGeneratorUser.class, false);
        copier.copy(userBean,user,null);
        user.setUpdateTime(LocalDateTime.now());
        int updateUser = userMapper.updateById(user);
        if (updateUser <= 0){
            throw new BizException("更新用户异常");
        }
        log.info("更新用户 updateUser:{}",updateUser);
        return ResultWrapper.getSuccessResultWrapper(null);
    }

    @Override
    public ResultWrapper<IdGeneratorUserBean> userDetail(Integer id) {
        log.info("查询用户  入参:{}",id);

        IdGeneratorUserBean userBean = new IdGeneratorUserBean();
        IdGeneratorUser user = userMapper.selectById(id);
        if (user == null){
            throw new BizException("查询无此用户");
        }
        log.info("查询用户 user:{}",new Gson().toJson(user));
        BeanCopier copier = BeanCopier.create(IdGeneratorUser.class, IdGeneratorUserBean.class, false);
        copier.copy(user,userBean,null);
        IdGeneratorGroup group = groupMapper.selectById(user.getGroupId());
        userBean.setGroupName(group.getGroupName());
        return ResultWrapper.getSuccessResultWrapper(userBean);
    }
}
