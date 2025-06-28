package org.bx.idgenerator.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020/10/30 17:24
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@ApiModel(value="单个body参数实体类")
public class SingleBodyDTO<T> {

    @ApiModelProperty(value = "参数值")
    T value;

    public T getValue() {
        return value;
    }
}
