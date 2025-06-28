package org.bx.idgenerator.core.server;



import com.baomidou.mybatisplus.extension.service.IService;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.base.segment.entity.LeafNode;


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

  ResultWrapper<LeafNode> findBySystemIDAndBizTag(String systemId, String bizTag);


}
