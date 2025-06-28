package org.bx.idgenerator.manager.db.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.bx.idgenerator.bean.LoginUserBean;
import org.bx.idgenerator.manager.entity.IdGeneratorUser;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 马士兵 · 项目架构部
 * @since 2020-07-08
 */
public interface IdGeneratorUserMapper extends BaseMapper<IdGeneratorUser> {

    LoginUserBean login(@Param("user") String username, @Param("pwd") String password);

}
