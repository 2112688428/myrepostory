package org.bx.idgenerator.core.dingding;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 钉钉机器人工具类
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月06日 12时28分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Slf4j
public class DingRobotUtil {

    private DingRobotUtil() {

    }

    public static boolean sendMsg(MsbMessageDTO message) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, ApiException {
        String webhookUrl = message.getDdWebhookUrl();
        // 加签
        if (message.isDdNeedSign() && message.getDdSecret() != null) {
            Long timestamp = System.currentTimeMillis();
            String sign = getSign(message.getDdSecret(), timestamp);
            webhookUrl += "&timestamp=" + timestamp + "&sign=" + sign;
        }
        DingTalkClient client = new DefaultDingTalkClient(webhookUrl);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(message.getContent());
        request.setText(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(message.getDdMobileList());
        // isAtAll类型如果不为Boolean，请升级至最新SDK
        at.setIsAtAll(message.isDdAtAll());
        request.setAt(at);
        OapiRobotSendResponse response = client.execute(request);
        if (log.isDebugEnabled()) {
            log.debug("DingdingRobotSend message:{}, response:{}", message, response.getBody());
        }
        return true;
    }

    public static String getSign(String secret, Long timestamp) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        //拼接
        String stringToSign = timestamp + "\n" + secret;
        //使用HmacSHA256算法计算签名
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        //进行Base64 encode 得到最后的sign，可以拼接进url里
        return URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
    }
}
