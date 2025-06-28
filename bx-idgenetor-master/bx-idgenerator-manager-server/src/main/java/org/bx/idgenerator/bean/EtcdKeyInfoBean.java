package org.bx.idgenerator.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
public class EtcdKeyInfoBean implements Serializable {


    /**
     * 组id
     */
    private Integer groupId;

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

}

