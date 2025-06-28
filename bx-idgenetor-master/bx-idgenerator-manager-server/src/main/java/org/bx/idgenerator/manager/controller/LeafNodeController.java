package org.bx.idgenerator.manager.controller;

import com.msb.cube.common.base.dto.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bx.idgenerator.bean.NodeTypeEnum;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.bean.SingleBodyDTO;
import org.bx.idgenerator.bean.SnowFlakeEndPointDTO;
import org.bx.idgenerator.exception.BizException;
import org.bx.idgenerator.manager.config.LoginAuthToken;
import org.bx.idgenerator.manager.dto.LeafNodeDTO;
import org.bx.idgenerator.manager.dto.LeafNodePageDTO;
import org.bx.idgenerator.manager.entity.LeafNode;
import org.bx.idgenerator.manager.service.IEtcdNodeOperateService;
import org.bx.idgenerator.manager.service.ILeafNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact
 * @date 2020-10-22
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Api(value = "segment控制器",tags = " segment控制器")
@RestController
@RequestMapping("/generator/segment/manager")
@Validated
public class LeafNodeController {


    @Autowired
    private ILeafNodeService leafNodeService;

    @Autowired
    private IEtcdNodeOperateService etcdNodeOperateService;

    @ApiOperation(value = "根据条件分页查询节点",notes = "根据条件分页查询节点")
    @PostMapping("/page/list-node")
    public ResultWrapper<PageResult<LeafNode>>  pageList(@RequestBody LeafNodePageDTO leafNodePageDTO){
        return ResultWrapper.getSuccessResultWrapper(leafNodeService.pageList(leafNodePageDTO));
    }

    @ApiOperation(value = "新增节点",notes = "新增节点")
    @PostMapping("/add-node")
    public ResultWrapper<String> add(@RequestBody LeafNodeDTO leafNode){
        return leafNodeService.add(leafNode);
    }

    @ApiOperation(value = "删除节点",notes = "删除节点")
    @PostMapping("/del-node")
    public ResultWrapper<String> delete(@RequestBody @NotNull SingleBodyDTO<Integer> id){
        Integer value = id.getValue();
        if(value == null ){
            throw new BizException("id不能为空");
        }
        return leafNodeService.doDelete(value);
    }

    @ApiOperation(value = "禁用节点",notes = "禁用节点")
    @PostMapping("/deactivate-node")
    public ResultWrapper<String> doDeactivate(@RequestBody @NotNull SingleBodyDTO<Integer> id){
        Integer value = id.getValue();
        if(value == null ){
            throw new BizException("id不能为空");
        }
        return leafNodeService.doDeactivate(value);
    }

    @ApiOperation(value = "启用节点",notes = "启用节点")
    @PostMapping("/enable-node")
    public ResultWrapper<String> doEnabled(@RequestBody @NotNull SingleBodyDTO<Integer> id){
        Integer value = id.getValue();
        if(value == null ){
            throw new BizException("id不能为空");
        }
        return leafNodeService.doEnabled(value);
    }

    @ApiOperation(value = "更新节点",notes = "更新节点")
    @PostMapping("/update-node")
    public ResultWrapper<String> update(@RequestBody LeafNodeDTO leafNode){
        return leafNodeService.doUpdateByManager(leafNode);
    }




    @ApiOperation(value = "查询当前登录人能看到的全部节点",notes = "查询当前登录人能看到的全部节点")
    @GetMapping("/curr-user-all-list")
    @LoginAuthToken
    public ResultWrapper<List<LeafNode>>  listByCurrent(){
        return ResultWrapper.getSuccessResultWrapper(leafNodeService.listByCurrent());
    }


    @ApiOperation(value = "查询snowflake节点",notes = "查询snowflake节点")
    @GetMapping("/snowflake/node")
    public ResultWrapper<List<SnowFlakeEndPointDTO>> getAll(){
       return etcdNodeOperateService.selectSnowFlakeEndPoint(NodeTypeEnum.FOREVER);
    }

}
