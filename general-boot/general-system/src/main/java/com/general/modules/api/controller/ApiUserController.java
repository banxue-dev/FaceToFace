package com.general.modules.api.controller;

import com.general.aop.log.Log;
import com.general.common.Result;
import com.general.config.DataScope;
import com.general.exception.BadRequestException;
import com.general.modules.api.domain.vo.UserLoginVo;
import com.general.modules.security.security.AuthenticationInfo;
import com.general.modules.security.security.JwtUser;
import com.general.modules.security.utils.JwtTokenUtil;
import com.general.modules.system.domain.User;
import com.general.modules.system.domain.vo.UserPassVo;
import com.general.modules.system.service.DeptService;
import com.general.modules.system.service.RoleService;
import com.general.modules.system.service.UserService;
import com.general.modules.system.service.dto.RoleSmallDTO;
import com.general.modules.system.service.dto.UserDTO;
import com.general.modules.system.service.dto.UserQueryCriteria;
import com.general.utils.EncryptUtils;
import com.general.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Api(tags = "外部接口-用户管理接口")
@RestController
@RequestMapping("pass")
public class ApiUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private DataScope dataScope;
    @Autowired
    private DeptService deptService;
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RoleService roleService;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

//    @Log("客户端-修改定位开关")
//    @ApiOperation(value = "修改定位开关")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "long"),
//            @ApiImplicitParam(name = "status", value = "状态：0开，1关", required = true, paramType = "path", dataType = "int")
//    })
//    @PutMapping(value = "/updateLocationSwitch/{id}/{status}")
//    public Result updateLocationSwitch(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
//        userService.updateLocationSwitch(id, status);
//        return new Result();
//    }
//
//    @Log("客户端-修改视频开关")
//    @ApiOperation(value = "修改视频开关")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "long"),
//            @ApiImplicitParam(name = "status", value = "状态：0开，1关", required = true, paramType = "path", dataType = "int")
//    })
//    @PutMapping(value = "/updateVideoSwitch/{id}/{status}")
//    public Result updateVideoSwitch(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
//        userService.updateVideoSwitch(id, status);
//        return new Result();
//    }

    @Log("客户端-根据组织ID查询用户信息")
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

    @Log("客户端-用户登录")
    @PostMapping(value = "/login")
    public ResponseEntity login(@Validated @RequestBody UserLoginVo authorizationUser) {
        final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(authorizationUser.getUsername());
//        if(jwtUser.getUserType()==0) {
//        	 throw new AccountExpiredException("普通用户无法使用本系统");
//        }
        if (!jwtUser.getPassword().equals(EncryptUtils.encryptPassword(authorizationUser.getPassword()))) {
            throw new AccountExpiredException("密码错误");
        }
        if (!jwtUser.isEnabled()) {
            throw new AccountExpiredException("账号已停用，请联系管理员");
        }
        // 生成令牌
        final String token = jwtTokenUtil.generateToken(jwtUser);

        // 返回 token
        return ResponseEntity.ok(new AuthenticationInfo(token, jwtUser));
    }

    @Log("客户端-修改密码")
    @ApiOperation(value = "修改密码")
    @PostMapping(value = "/updatePass")
    public ResponseEntity updatePass(@RequestBody UserPassVo user) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        if (!userDetails.getPassword().equals(EncryptUtils.encryptPassword(user.getOldPass()))) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if (userDetails.getPassword().equals(EncryptUtils.encryptPassword(user.getNewPass()))) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePass(userDetails.getUsername(), EncryptUtils.encryptPassword(user.getNewPass()));
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("客户端-修改用户")
    @ApiOperation(value = "修改用户")
    @PutMapping(value = "/updateUsers")
    public ResponseEntity update(@Validated(User.Update.class) @RequestBody User resources) {
        checkLevel(resources);
        userService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param resources
     */
    private void checkLevel(User resources) {
        Integer currentLevel = Collections.min(roleService.findByUsers_Id(SecurityUtils.getUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
        Integer optLevel = roleService.findByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }

    @Log("状态管理器-查询所有用户")
    @ApiOperation(value = "状态管理器-查询所有用户")
    @GetMapping(value = "/listUsers")
    public ResponseEntity listUsers() {
        return new ResponseEntity(userService.queryAll(null), HttpStatus.OK);
    }
}
