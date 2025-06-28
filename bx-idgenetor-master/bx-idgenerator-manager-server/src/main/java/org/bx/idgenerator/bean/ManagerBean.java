package org.bx.idgenerator.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020/7/8/7:21
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
@ApiModel(value = "新增节点入参")
public class ManagerBean implements Serializable {

    @ApiModelProperty(value = "组id")
    private Integer groupId;

    @ApiModelProperty(value = "节点地址")
    private String bizTag;

    @ApiModelProperty(value = "步长")
    private Integer step;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "目前最大id")
    private Integer maxId;

}
