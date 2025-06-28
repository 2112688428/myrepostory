package org.bx.idgenerator.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020/7/10/9:57
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@ApiModel(value = "登录用户信息VO")
@Data
@EqualsAndHashCode(callSuper = false)
public class LoginUserBean implements Serializable {

    @ApiModelProperty(value = "用户主键")
    private Integer id;

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String telephone;

    @ApiModelProperty(value = "是否禁用 1-是,0-否")
    private Boolean enabled;

    @ApiModelProperty(value = "组id")
    private Integer groupId;

    @ApiModelProperty(value = "组名称 ")
    private String groupName;

    @ApiModelProperty(value = "组节点")
    private List<String> bizTags;

    @ApiModelProperty(value = "登录成功token")
    private String token;
}
