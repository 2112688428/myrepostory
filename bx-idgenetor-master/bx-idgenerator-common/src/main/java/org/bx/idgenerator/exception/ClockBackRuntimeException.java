package org.bx.idgenerator.exception;

/**
 * 时钟回拨异常
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月07日 17时37分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public class ClockBackRuntimeException extends RuntimeException{
    public ClockBackRuntimeException(String message) {
        super(message);
    }
}
