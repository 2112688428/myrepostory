package org.bx.idgenerator.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.msb.cube.common.base.dto.PageResult;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.manager.dto.LeafNodeDTO;
import org.bx.idgenerator.manager.dto.LeafNodePageDTO;
import org.bx.idgenerator.manager.entity.LeafNode;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact
 * @date 2020-10-22
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public interface ILeafNodeService extends IService<LeafNode> {


  PageResult<LeafNode> pageList(LeafNodePageDTO leafNodePageDTO);


  List<LeafNode> listByCurrent();


  ResultWrapper<String> add(LeafNodeDTO leafNode);

  ResultWrapper<String> doDelete(Integer id);


  ResultWrapper<String> doDeactivate(Integer id);

  ResultWrapper<String> doEnabled(Integer id);

  ResultWrapper<String> doUpdateByManager(LeafNodeDTO leafNode);

  ResultWrapper<LeafNode> detail(Long id);

  ResultWrapper<LeafNode> findBySystemIDAndBizTag(String systemId, String bizTag);


}
