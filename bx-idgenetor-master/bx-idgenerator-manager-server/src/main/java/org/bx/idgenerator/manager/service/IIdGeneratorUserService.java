package org.bx.idgenerator.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.msb.cube.common.base.dto.PageResult;
import org.bx.idgenerator.bean.IdGeneratorUserBean;
import org.bx.idgenerator.bean.LoginUserBean;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.manager.entity.IdGeneratorUser;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact
 * @date 2020-07-08
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public interface IIdGeneratorUserService extends IService<IdGeneratorUser> {

  /**
   * 分页查询用户
   * @param pageNum
   * @param pageSize
   * @return PageResult<IdGeneratorUserBean>
   * */
  ResultWrapper<PageResult<IdGeneratorUserBean>> pageListUser(Integer pageNum, Integer pageSize);

  /**
   * 登录
   * @param username
   * @param password
   * @param request
   * @return LoginUserBean
   * */
  ResultWrapper<LoginUserBean> loginUser(String username, String password, HttpServletRequest request);

  /**
   * 新增用户
   * @param userBean
   * @return Integer
   * */
  ResultWrapper<Integer> addUser(IdGeneratorUserBean userBean);

  /**
   * 删除用户
   * @param id
   * @return Integer
   * */
  ResultWrapper<Integer> deleteUser(Integer id);

  /**
   * 更新用户信息
   * @param userBean
   * @return Integer
   * */
  ResultWrapper<Integer> updateUser(IdGeneratorUserBean userBean);

  /**
   * 查询用户详细信息
   * @param id
   * @return IdGeneratorUserBean
   * */
  ResultWrapper<IdGeneratorUserBean> userDetail(Integer id);
}
