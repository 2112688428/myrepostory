package org.bx;

import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.bean.IdGenerateBean;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.core.dingding.MessageService;
import org.bx.idgenerator.core.dingding.UuidUtil;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020/12/1 14:28
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Slf4j
@Deprecated
public class IdGenerateServiceWrapper {


    private IdGenerateFeignClient client;


    private MessageService messageService;


    /**
     * segmentId
     *
     * @param idGenerateBean
     * @return ResultWrapper
     */
    public ResultWrapper<String> getSegmentId(IdGenerateBean idGenerateBean) {
        ResultWrapper<String> result = null;
        boolean hasError = false;
        try {
            result = client.getSegmentId(idGenerateBean);
        } catch (Exception e) {
            log.error("调用出现异常：{}", e);
            hasError = true;
            messageService.sendMessage(idGenerateBean + ";" + e.getMessage() + ";" + e.getCause().toString());
        }
        if (hasError) {
            result = ResultWrapper.getSuccessResultWrapper(UuidUtil.getUid());
        }
        return result;
    }

    /**
     * snowflakeId
     *
     * @return ResultWrapper
     */
    public ResultWrapper<String> getSnowflakeId() {
        ResultWrapper<String> result = null;
        try {
            result = client.getSnowflakeId();
        } catch (Exception e) {
            messageService.sendMessage(e.getMessage());
        }
        return result;
    }


}
