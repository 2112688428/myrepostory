package org.bx.idgenerator.bean;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年05月28日 00时17分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public enum StateCode {
    SUCCESS(200, "请求成功"),

    CONSTRAINT_VIOLATION(502, "方法参数校验出错"),

    METHOD_ARGUMENT_NOT_VALID(501, "方法参数无效"),

    ERROR(500, "服务器异常");


    private String msg;
    private int code;

    public String msg() {
        return msg;
    }

    public int code() {
        return code;
    }

    StateCode(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

}
