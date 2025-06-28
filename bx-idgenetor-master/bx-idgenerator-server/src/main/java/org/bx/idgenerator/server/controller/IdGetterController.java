package org.bx.idgenerator.server.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bx.idgenerator.Constants;
import org.bx.idgenerator.bean.IdGenerateBean;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.core.server.IdGetterService;
import org.bx.idgenerator.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 对外提供的获取id服务
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 14:00
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@RestController
@Slf4j
@Validated
@RequestMapping("/generator/id")
@Api(value = "id服务", tags = "id服务")
public class IdGetterController {


    @Autowired(required = false)
    @Qualifier("segmentServiceImpl")
    IdGetterService segmentServiceImpl;

    @Autowired(required = false)
    @Qualifier("snowflakeServiceImpl")
    IdGetterService snowflakeServiceImpl;

    /**
     * ID分隔符
     */
    public static final String ID_SEPARATOR = "_";

    @PostMapping(value = "/api/segment")
    @ApiOperation(value = "segment获取id")
    public ResultWrapper<String> getSegmentId(@RequestBody IdGenerateBean idGenerateBean) {
        if (segmentServiceImpl == null) {
            throw new BizException("请通过bx.idgenerator.segment=true开启segment");
        }
        String systemId = idGenerateBean.getSystemId();
        String bizTag = idGenerateBean.getBizTag();
        if(StringUtils.isEmpty(systemId)){
            throw new BizException("systemId不能为空");
        }
        if(StringUtils.isEmpty(bizTag)){
            throw new BizException("bizTag不能为空");
        }
        Integer size = idGenerateBean.getSize();
        if (size == null || size < 0 || size > 20) {
            size = 8;
        }

        long id = segmentServiceImpl.getId(Constants.getKeyBySysIdAndTagNew(idGenerateBean));
        log.info("segment key：{}，拿到的id:{}", idGenerateBean,id);
        if (String.valueOf(id).length() > size) {
            // todo 超过最大值钉钉报警
            return ResultWrapper.getErrorResultWrapper("Id超过最大值");
        }
        String ids = String.format("%0" + size + "d", id);
        String result = bizTag + ID_SEPARATOR + ids;
        return ResultWrapper.getSuccessResultWrapper(result);
    }

    @PostMapping(value = "/api/snowflake")
    @ApiOperation(value = "雪花算法获取id")
    public ResultWrapper<String> getSnowflakeId() {

        if (snowflakeServiceImpl == null) {
            throw new BizException("请通过bx.idgenerator.segment=true开启snowflake");
        }

        long id = snowflakeServiceImpl.getId(null);
        log.info(" snowflake 拿到的id:{}", id);
        String ids = String.valueOf(id);
        return ResultWrapper.getSuccessResultWrapper(ids);
    }

    @PostMapping(value = "/api/segment/delete")
    @ApiOperation(value = "删除节点")
    public ResultWrapper<String> delSegment(@RequestBody IdGenerateBean idGenerateBean) {
        if (segmentServiceImpl == null) {
            throw new BizException("请通过bx.idgenerator.segment=true开启snowflake");
        }
        String systemId = idGenerateBean.getSystemId();
        String bizTag = idGenerateBean.getBizTag();
        if(StringUtils.isEmpty(systemId)){
            throw new BizException("systemId不能为空");
        }
        if(StringUtils.isEmpty(bizTag)){
            throw new BizException("bizTag不能为空");
        }
        return ResultWrapper.getSuccessResultWrapper(null);
    }

    /**
     * 获取机器的当前时间戳
     *
     * @return
     */
    @GetMapping(value = "/api/time")
    public String getTime() {
        Long currentTimeMillis = System.currentTimeMillis();
        log.info("当前时间: {}", currentTimeMillis);
        return currentTimeMillis.toString();
    }
}
