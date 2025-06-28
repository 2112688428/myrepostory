package org.bx.idgenerator.manager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact
 * @date 2020-07-09
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("id_generator_group")

public class IdGeneratorGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 是否启用：1-启用；0-禁用
     */
    private Boolean enable;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
