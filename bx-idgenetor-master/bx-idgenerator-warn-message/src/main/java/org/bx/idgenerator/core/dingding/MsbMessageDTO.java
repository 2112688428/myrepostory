package org.bx.idgenerator.core.dingding;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liweizhi
 * @date 2020/11/7
 */
@Data
public class MsbMessageDTO implements Serializable {
    static final long serialVersionUID = 42L;

    /**
     * 发送方式
     */
    private Integer channel;

    /**
     * 内容
     */
    private String content;

    // -----------------------短信特有的参数 start--------------------------------
    /**
     * 我们定义的msbTemplateMap中的key
     */
    private String msbTemplateKey;
    /**
     * 短信模板中的参数值
     */
    private String[] templateParamValues;
    /**
     * 短信接收者手机号
     */
    private String[] phones;
    // -----------------------短信特有的参数 end--------------------------------


    // -----------------------邮件特有的参数 start--------------------------------
    /**
     * 邮件接收人
     */
    private String[] mailTo;
    /**
     * 邮件抄送人
     */
    private String[] mailCc;
    /**
     * 邮件主题
     */
    private String mailSubject;
    // -----------------------钉钉特有的参数 start--------------------------------
    /**
     * 是否@所有人 --钉钉机器人消息
     */
    private boolean ddAtAll;
    /**
     * 如果isAtAll为false,则指定@哪些人 --钉钉机器人消息
     */
    private List<String> ddMobileList;
    /**
     * 是否需要加签名
     */
    private boolean ddNeedSign;
    /**
     * 钉钉机器人-安全设置中,加签的密钥 --钉钉机器人消息
     */
    private String ddSecret;
    /**
     * 使用该Webhook地址,向钉钉群推送消息 --钉钉机器人消息
     */
    private String ddWebhookUrl;
    // -----------------------钉钉特有的参数 end--------------------------------

    /**
     * @param channel    发送方式
     * @param content    内容
     * @param isAtAll    是否@所有人
     * @param mobileList 如果isAtAll为false,则指定@哪些人
     * @param secret     钉钉机器人-安全设置中,加签的密钥
     * @param webhookUrl 使用该Webhook地址,向钉钉群推送消息
     * @return 钉钉机器人消息
     */
    public static MsbMessageDTO newDingDingMsg(Integer channel, String content, boolean isAtAll, List<String> mobileList, boolean needSign, String secret, String webhookUrl) {
        MsbMessageDTO msg = new MsbMessageDTO();
        msg.channel = channel;
        msg.content = content;
        msg.ddAtAll = isAtAll;
        msg.ddMobileList = mobileList;
        msg.ddNeedSign = needSign;
        msg.ddSecret = secret;
        msg.ddWebhookUrl = webhookUrl;
        return msg;
    }

    /**
     * @param channel
     * @param mailSubject 主题
     * @param content     内容
     * @param mailTo      收件人
     * @param mailCc      抄送人
     */
    public static MsbMessageDTO newMailMsg(Integer channel, String mailSubject, String content, String[] mailTo, String[] mailCc) {
        MsbMessageDTO msg = new MsbMessageDTO();
        msg.channel = channel;
        msg.mailSubject = mailSubject;
        msg.content = content;
        msg.mailTo = mailTo;
        msg.mailCc = mailCc;
        return msg;
    }

    /**
     * @param channel
     * @param phones              接收人的手机号数组
     * @param msbTemplateKey      我们定义的msbTemplateMap中的key
     * @param templateParamValues 短信模板中的参数值
     */
    public static MsbMessageDTO newSmsMsg(Integer channel, String[] phones, String msbTemplateKey, String[] templateParamValues) {
        MsbMessageDTO msg = new MsbMessageDTO();
        msg.channel = channel;
        msg.msbTemplateKey = msbTemplateKey;
        msg.templateParamValues = templateParamValues;
        msg.phones = phones;
        return msg;
    }
}
