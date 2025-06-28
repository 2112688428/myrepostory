package org.bx.idgenerator.base.getter.snowflake.notify;

import org.bx.idgenerator.base.getter.snowflake.bean.NotifyContext;

/**
 * 失败通知接口
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月14日 14时56分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public interface INotifyService {

    /**
     * 通知时钟回滚异常
     *
     * @param context 上下文信息
     */
    <T> void notify(NotifyContext<T> context) throws Exception;
}
