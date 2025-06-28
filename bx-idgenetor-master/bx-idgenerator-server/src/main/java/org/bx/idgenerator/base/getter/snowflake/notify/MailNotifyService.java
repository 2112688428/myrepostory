package org.bx.idgenerator.base.getter.snowflake.notify;

import org.bx.idgenerator.base.getter.snowflake.bean.NotifyContext;
import org.bx.idgenerator.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 异常邮件通知服务
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月14日 15时01分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Component
public class MailNotifyService implements INotifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailNotifyService.class);

    @Override
    public void notify(NotifyContext context) throws Exception {
        LOGGER.error("ID生成器发生错误,错误信息:{}", context);
        String content = context.getMsg();
        String subject = "分布式ID服务时钟回拨通知邮件";
        // TODO
        throw new BizException("服务节点发生时钟回拨问题");
    }
}
