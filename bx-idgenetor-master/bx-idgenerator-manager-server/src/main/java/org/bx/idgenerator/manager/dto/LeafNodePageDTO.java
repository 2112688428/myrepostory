package org.bx.idgenerator.manager.dto;

import com.msb.cube.common.base.dto.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020/12/9 14:16
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@ApiModel(value = "segment节点查询分页参数", description = "segment节点查询分页参数")
@Data
public class LeafNodePageDTO extends BasePageRequest implements Serializable {
    @ApiModelProperty(value = "组Id")
    private Integer groupId;

    @ApiModelProperty(value = "系统Id")
    private String systemId;

    @ApiModelProperty(value = "业务标识")
    private String bizTag;

    @ApiModelProperty(value = "是否启用")
    private Boolean enableFlag;



}
