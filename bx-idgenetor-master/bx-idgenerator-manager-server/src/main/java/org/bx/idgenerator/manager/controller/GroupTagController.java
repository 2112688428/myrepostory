package org.bx.idgenerator.manager.controller;

import com.msb.cube.common.base.dto.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bx.idgenerator.bean.GroupTagBean;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.manager.service.IGroupTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact
 * @date 2020-07-09
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Api(value = "组管理",tags = "组管理")
@RestController
@RequestMapping("/ums/groupTag")
@Validated
public class GroupTagController {


    @Autowired
    private IGroupTagService groupTagService;


    @ApiOperation(value = "分页查询")
    @GetMapping("/list/{groupId}")
    public ResultWrapper<PageResult<GroupTagBean>> pageListTag(@PathVariable("groupId") Integer groupId, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        return groupTagService.pageListTag(groupId,pageNum,pageSize);
    }


    @ApiOperation(value = "添加")
    @PostMapping("/add")
    public ResultWrapper<Integer> addTag(@Valid @RequestBody GroupTagBean tagBean){
        return groupTagService.addTag(tagBean);
    }


    @ApiOperation(value = "删除")
    @ApiParam(name = "id",value = "id",required = true)
    @PostMapping("/delete/{id}")
    public ResultWrapper<Integer> deleteTag(@NotNull(message = "ID不能为空") @PathVariable("id") Integer id){
        return groupTagService.deleteTag(id);
    }


    @ApiOperation(value = "更新")
    @PostMapping("/update")
    public ResultWrapper<Integer> updateTag(@Valid @RequestBody GroupTagBean tagBean){
        return groupTagService.updateTag(tagBean);
    }

    @ApiOperation(value = "查询详情")
    @ApiParam(name = "id",value = "id",required = true)
    @GetMapping("/detail/{id}")
    public ResultWrapper<GroupTagBean> tagDetail(@NotNull(message = "ID不能为空") @PathVariable("id") Integer id){
        return groupTagService.tagDetail(id);
    }
}
