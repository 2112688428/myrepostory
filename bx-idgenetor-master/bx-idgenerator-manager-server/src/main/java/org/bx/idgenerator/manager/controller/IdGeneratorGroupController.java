package org.bx.idgenerator.manager.controller;

import com.msb.cube.common.base.dto.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bx.idgenerator.bean.IdGeneratorGroupBean;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.manager.service.IIdGeneratorGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact
 * @date 2020-07-08
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Api(value = "组-节点管理",tags = "组-节点管理")
@RestController
@RequestMapping("/manager/idGeneratorGroup")
@Validated
public class IdGeneratorGroupController {


    @Autowired
    private IIdGeneratorGroupService idGeneratorGroupService;


    @ApiOperation(value = "分页查询")
    @GetMapping("/list")
    public ResultWrapper<PageResult<IdGeneratorGroupBean>> listGroup(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        return idGeneratorGroupService.listGroup(pageNum,pageSize);
    }


    @ApiOperation(value = "添加")
    @PostMapping("/add")
    public ResultWrapper<Integer> addGroup(@RequestBody IdGeneratorGroupBean groupBean){
        return idGeneratorGroupService.addGroup(groupBean);
    }


    @ApiOperation(value = "删除")
    @ApiParam(name = "id",value = "id",required = true)
    @DeleteMapping("/delete/{id}")
    public ResultWrapper<Integer> deleteGroup(@NotNull(message = "ID不能为空") @PathVariable("id") Integer id){
        return idGeneratorGroupService.deleteGroup(id);
    }


    @ApiOperation(value = "更新")
    @PostMapping("/update")
    public ResultWrapper<Integer> updateGroup(@RequestBody IdGeneratorGroupBean groupBean){
        return idGeneratorGroupService.updateGroup(groupBean);
    }

    @ApiOperation(value = "查询详情")
    @ApiParam(name = "id",value = "id",required = true)
    @GetMapping("/detail/{id}")
    public ResultWrapper<IdGeneratorGroupBean> groupDetail(@NotNull(message = "ID不能为空")@PathVariable("id") Integer id){
        return idGeneratorGroupService.groupDetail(id);
    }
}
