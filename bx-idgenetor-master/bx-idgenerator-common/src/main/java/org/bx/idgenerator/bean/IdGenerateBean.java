package org.bx.idgenerator.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *  节点信息
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020/7/8/7:21
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
@ApiModel(value="获取分布式id时的请求参数", description="获取分布式id时的请求参数")
public class IdGenerateBean implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "系统code")
    private String systemId;


    @ApiModelProperty(value = "业务键")
    private String bizTag;


    @ApiModelProperty(value = "id长度")
    private Integer size;


}
