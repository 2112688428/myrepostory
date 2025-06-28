package org.bx.idgenerator.exception;

import lombok.Data;
import org.bx.idgenerator.bean.StateCode;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 18:10
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
public class BizException extends RuntimeException {

    private String msg;
    private int code;


    public BizException(StateCode stateCode, Throwable throwable) {
        super(throwable);
        this.msg = stateCode.msg();
        this.code = stateCode.code();
    }

    public BizException(String msg, Throwable throwable) {
        super(throwable);
        this.msg = msg;
        this.code = StateCode.ERROR.code();
    }

    public BizException(String msg) {
        this.msg = msg;
        this.code = StateCode.ERROR.code();
    }

    public BizException(StateCode stateCode) {
        this(stateCode, null);
    }

}
