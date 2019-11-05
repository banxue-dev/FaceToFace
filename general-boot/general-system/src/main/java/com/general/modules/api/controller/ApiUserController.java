package com.general.modules.api.controller;

import com.general.aop.log.Log;
import com.general.common.Result;
import com.general.config.DataScope;
import com.general.modules.system.domain.User;
import com.general.modules.system.service.DeptService;
import com.general.modules.system.service.UserService;
import com.general.modules.system.service.dto.UserDTO;
import com.general.modules.system.service.dto.UserQueryCriteria;
import com.general.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 外部用户管理接口
 * @Author LuoJing
 * @Date 2019/11/1 16:07
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("pass")
public class ApiUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private DataScope dataScope;

    @Autowired
    private DeptService deptService;

    @Log("修改定位开关")
    @ApiOperation(value = "修改定位开关")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "long"),
            @ApiImplicitParam(name = "status", value = "状态：0开，1关", required = true, paramType = "path", dataType = "int")
    })
    @PutMapping(value = "/updateLocationSwitch/{id}/{status}")
    public Result updateLocationSwitch(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        userService.updateLocationSwitch(id, status);
        return new Result();
    }

    @Log("修改视频开关")
    @ApiOperation(value = "修改视频开关")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "long"),
            @ApiImplicitParam(name = "status", value = "状态：0开，1关", required = true, paramType = "path", dataType = "int")
    })
    @PutMapping(value = "/updateVideoSwitch/{id}/{status}")
    public Result updateVideoSwitch(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        userService.updateVideoSwitch(id, status);
        return new Result();
    }

    @Log("根据组织ID查询用户信息")
    @ApiOperation(value = "根据组织ID查询用户信息")
    @ApiImplicitParam(name = "deptId", value = "用户ID", required = true, paramType = "path", dataType = "long")
    @GetMapping("/listUserByDeptId/{deptId}")
    public Result<List<UserDTO>> listUserByDeptId(@PathVariable("deptId") Long deptId) {
        UserQueryCriteria criteria = new UserQueryCriteria();
        criteria.setDeptId(deptId);
        Set<Long> deptSet = new HashSet<>();
        Set<Long> result = new HashSet<>();
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
        }
        result.addAll(deptSet);
        criteria.setDeptIds(result);
        List<UserDTO> userDTOList = userService.queryAll(criteria);
        return new Result(userDTOList);
    }

}
