package org.bx.idgenerator.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.msb.cube.common.base.dto.PageResult;
import org.bx.idgenerator.bean.IdGeneratorGroupBean;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.manager.entity.IdGeneratorGroup;

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
public interface IIdGeneratorGroupService extends IService<IdGeneratorGroup> {

  /**
   * 分页查询组
   * @param pageNum
   * @param pageSize
   * @return PageResult<IdGeneratorGroupBean>
   * */
  ResultWrapper<PageResult<IdGeneratorGroupBean>> listGroup(Integer pageNum, Integer pageSize);

  /**
   * 新增组
   * @param groupBean
   * @return Integer
   * */
  ResultWrapper<Integer> addGroup(IdGeneratorGroupBean groupBean);

  /**
   * 删除组
   * @param id
   * @return Integer
   * */
  ResultWrapper<Integer> deleteGroup(Integer id);

  /**
   * 更新组
   * @param groupBean
   * @return Integer
   * */
  ResultWrapper<Integer> updateGroup(IdGeneratorGroupBean groupBean);

  /**
   * 组详情
   * @param id
   * @return IdGeneratorGroupBean
   * */
  ResultWrapper<IdGeneratorGroupBean> groupDetail(Integer id);
}
