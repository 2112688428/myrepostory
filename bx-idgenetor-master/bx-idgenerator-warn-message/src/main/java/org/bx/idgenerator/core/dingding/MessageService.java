package org.bx.idgenerator.core.dingding;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020/11/30 18:41
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Slf4j
@Component
@ToString
public class MessageService {

    /**
     * 单个线程就行了
     */
    ExecutorService executor = Executors.newSingleThreadExecutor();


    AtomicLong time = new AtomicLong();

    @Value("${ddrobot.secret}")
    private String ddSecret ;

    @Value("${ddrobot.hookurl}")
    private String ddWebhookUrl ;


    public void sendMessage(String msg){
        long l = time.get();

        long currTime = System.currentTimeMillis();
        long l1 = l + 60 * 1000;
        if(l1 > currTime){
            log.error("一分钟之内只能发送一次消息：{}",msg);
            return;
        }

        boolean b = time.compareAndSet(l, currTime);
        if(!b){
            return;
        }
        executor.submit(()->{
            MsbMessageDTO msbMessageDTO = new MsbMessageDTO();
            msbMessageDTO.setDdWebhookUrl(ddWebhookUrl);
            msbMessageDTO.setDdNeedSign(true);
            msbMessageDTO.setDdSecret(ddSecret);
            msbMessageDTO.setContent(msg);
            msbMessageDTO.setDdAtAll(true);

            try {
                DingRobotUtil.sendMsg(msbMessageDTO);
            }  catch (Exception e) {
                log.error("发消息出现异常:{}",msbMessageDTO,e);
                time.compareAndSet(currTime,l);
            }
        });
    }


}
