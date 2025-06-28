package org.bx.idgenerator.manager;

import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.bean.StateCode;
import org.bx.idgenerator.exception.BizException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

/**
 * 统一异常处理
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年05月28日 16时41分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {


    @ResponseBody
    @ExceptionHandler({BizException.class})
    public ResultWrapper bizException(BizException e) {
        log.error("出现异常:{}", e);
        return ResultWrapper.builder().code(e.getCode()).data(e.getMsg()).build();
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultWrapper methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("出现异常:{}", e);
        return ResultWrapper.builder().code(StateCode.METHOD_ARGUMENT_NOT_VALID.code()).msg(StateCode.METHOD_ARGUMENT_NOT_VALID.msg()).build();
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultWrapper constraintViolationException(ConstraintViolationException e) {
        log.error("出现异常:{}", e);
        return ResultWrapper.builder().code(StateCode.CONSTRAINT_VIOLATION.code()).msg(StateCode.CONSTRAINT_VIOLATION.msg()).build();
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResultWrapper exception(Exception e) {
        log.error("出现异常:{}", e);
        return ResultWrapper.builder().code(StateCode.ERROR.code()).msg(StateCode.ERROR.msg()).build();
    }

}
