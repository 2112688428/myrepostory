package org.bx;


import org.bx.idgenerator.bean.IdGenerateBean;
import org.bx.idgenerator.bean.ResultWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020/9/21/16:56
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@FeignClient(value = "ID-GENERATOR-SERVER")
public interface IdGenerateFeignClient {

    /**
     * segmentId
     *
     * @param idGenerateBean
     * @return ResultWrapper
     */
    @PostMapping("/generator/id/api/segment")
    ResultWrapper<String> getSegmentId(@RequestBody IdGenerateBean idGenerateBean);

    /**
     * snowflakeId
     *
     * @return ResultWrapper
     */
    @PostMapping("/generator/id/api/snowflake")
    ResultWrapper<String> getSnowflakeId();

}



