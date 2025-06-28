package org.bx.idgenerator.bean;

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
public class KeyInfoBean implements Serializable {

    private Integer id;

    /**
     * 组id
      */
    private Integer groupId;


    /**
     * 系统Id
     */
    private String systemId;

    /**
     * 业务键
     */
    private String bizTag;

    /**
     * 步长
      */
    private Integer step;



    /**
     * 描述
      */
    private String description;

    /**
     * 更新时间
      */
    private Long updateTime;

    /**
     * 目前最大id
      */
    private Integer maxId;

    /**
     * 是否可以使用
     */
    private Boolean status;


    /**
     * 填充0的长度
     */
    private Integer fillZero;

}
