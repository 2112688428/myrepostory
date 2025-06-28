package org.bx.idgenerator.manager.controller;

import com.msb.cube.common.base.dto.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bx.idgenerator.bean.IdGeneratorUserBean;
import org.bx.idgenerator.bean.LoginUserBean;
import org.bx.idgenerator.bean.ResultWrapper;
import org.bx.idgenerator.manager.CurrentContextHolder;
import org.bx.idgenerator.manager.service.IIdGeneratorUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
@Api(value = "用户管理",tags = "用户管理")
@RestController
@RequestMapping("/manager/idGeneratorUser")
@Validated
public class IdGeneratorUserController {


    @Autowired
    private IIdGeneratorUserService idGeneratorUserService;

    @ApiOperation(value = "查询")
    @GetMapping("/list")
    public ResultWrapper<PageResult<IdGeneratorUserBean>> pageListUser(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        return idGeneratorUserService.pageListUser(pageNum,pageSize);
    }

    @ApiOperation(value = "添加")
    @PostMapping("/add")
    public ResultWrapper<Integer> addUser(@RequestBody IdGeneratorUserBean userBean){
        return idGeneratorUserService.addUser(userBean);
    }


    @ApiOperation(value = "删除")
    @ApiParam(name = "id",value = "id",required = true)
    @DeleteMapping("/delete/{id}")
    public ResultWrapper<Integer> deleteUser(@NotNull(message = "ID不能为空") @PathVariable Integer id){
        return idGeneratorUserService.deleteUser(id);
    }


    @ApiOperation(value = "更新")
    @PostMapping("/update")
    public ResultWrapper<Integer> updateUser(@RequestBody IdGeneratorUserBean userBean){
        return idGeneratorUserService.updateUser(userBean);
    }

    @ApiOperation(value = "查询详情")
    @ApiParam(name = "id",value = "id",required = true)
    @GetMapping("/detail/{id}")
    public ResultWrapper<IdGeneratorUserBean> userDetail(@NotNull(message = "ID不能为空") @PathVariable Integer id){
        return idGeneratorUserService.userDetail(id);
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public ResultWrapper<LoginUserBean> loginUser(@RequestParam @NotNull(message = "用户名不能为空") String username, @RequestParam @NotNull(message = "密码不能为空") String password, HttpServletRequest request){
        return idGeneratorUserService.loginUser(username,password,request);
    }

    @ApiOperation(value = "根据token获取用户信息")
    @PostMapping("/info")
    public ResultWrapper<LoginUserBean> info(){
        LoginUserBean user = CurrentContextHolder.getUser();
        return ResultWrapper.getSuccessResultWrapper(user);
    }

    @ApiOperation(value = "退出")
    @PostMapping("/logout")
    public ResultWrapper<Boolean> logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return ResultWrapper.getSuccessResultWrapper(true);
    }
}
