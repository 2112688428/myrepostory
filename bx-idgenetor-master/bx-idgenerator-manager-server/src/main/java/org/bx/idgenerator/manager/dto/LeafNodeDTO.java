package org.bx.idgenerator.manager.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact
 * @date 2020-10-22
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
@ApiModel(value = "节点传输参数", description = "节点传输参数")
public class LeafNodeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "组Id")
    private Integer groupId;

    @ApiModelProperty(value = "系统id")
    private String systemId;

    @ApiModelProperty(value = "业务标识")
    private String bizTag;

    @ApiModelProperty(value = "最大Id")
    private Long maxId;

    @ApiModelProperty(value = "步长")
    @NotNull
    @Min(1)
    private Integer step;

    @ApiModelProperty(value = "是否启用")
    @NotNull
    private Boolean enableFlag;

    @ApiModelProperty(value = "备注")
    private String description;

}
