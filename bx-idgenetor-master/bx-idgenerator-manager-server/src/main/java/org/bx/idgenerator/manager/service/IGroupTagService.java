package org.bx.idgenerator.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.msb.cube.common.base.dto.PageResult;
import org.bx.idgenerator.bean.GroupTagBean;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.manager.entity.GroupTag;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact
 * @date 2020-07-09
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public interface IGroupTagService extends IService<GroupTag> {

  /**
   * 分页查询组节点
   * @param groupId
   * @param pageNum
   * @param pageSize
   * @return PageResult<GroupTagBean>
   * */
  ResultWrapper<PageResult<GroupTagBean>> pageListTag(Integer groupId, Integer pageNum, Integer pageSize);

  /**
   * 新增组节点
   * @param tagBean
   * @return Integer
   * */
  ResultWrapper<Integer> addTag(GroupTagBean tagBean);

  /**
   * 删除组节点信息
   * @param id
   * @return Integer
   * */
  ResultWrapper<Integer> deleteTag(Integer id);

  /**
   * 更新组节点
   * @param tagBean
   * @return Integer
   * */
  ResultWrapper<Integer> updateTag(GroupTagBean tagBean);

  /**
   * 查询组节点详细信息
   * @param id
   * @return GroupTagBean
   * */
  ResultWrapper<GroupTagBean> tagDetail(Integer id);
}
