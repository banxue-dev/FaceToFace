package com.general.modules.system.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.general.aop.log.Log;
import com.general.config.DataScope;
import com.general.domain.VerificationCode;
import com.general.exception.BadRequestException;
import com.general.modules.system.domain.User;
import com.general.modules.system.domain.vo.UserPassVo;
import com.general.modules.system.service.ChannelsInfoService;
import com.general.modules.system.service.DeptService;
import com.general.modules.system.service.RoleService;
import com.general.modules.system.service.UserService;
import com.general.modules.system.service.dto.DeptDTO;
import com.general.modules.system.service.dto.RoleSmallDTO;
import com.general.modules.system.service.dto.UserDTO;
import com.general.modules.system.service.dto.UserQueryCriteria;
import com.general.service.VerificationCodeService;
import com.general.utils.ElAdminConstant;
import com.general.utils.EncryptUtils;
import com.general.utils.PageUtil;
import com.general.utils.SecurityUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author L
 * @date 2018-11-23
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DataScope dataScope;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private ChannelsInfoService channelsInfoService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Log("导出用户数据")
    @GetMapping(value = "/users/download")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT')")
    public void update(HttpServletResponse response, UserQueryCriteria criteria) throws IOException {
        userService.download(userService.queryAll(criteria), response);
    }

    @Log("集合查询用户信息")
    @ApiOperation(value = "集合查询用户信息")
    @GetMapping(value = "/listUsers")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT')")
    public ResponseEntity listUsers(UserQueryCriteria criteria) {
        Set<Long> deptSet = new HashSet<>();
        Set<Long> result = new HashSet<>();
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
        }
        // 数据权限
        Set<Long> deptIds = dataScope.getDeptIds();
        // 查询条件不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(deptIds) && !CollectionUtils.isEmpty(deptSet)) {
            // 取交集
            result.addAll(deptSet);
            result.retainAll(deptIds);

            // 若无交集，则代表无数据权限
            criteria.setDeptIds(result);
            if (result.size() == 0) {
                return new ResponseEntity(PageUtil.toPage(null, 0), HttpStatus.OK);
            } else return new ResponseEntity(userService.queryAll(criteria), HttpStatus.OK);
            // 否则取并集
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            List<UserDTO> lst=userService.queryAll(criteria);
            for(UserDTO t:lst) {
            	if(t.getDefaultChannelsId()!=null) {
            		t.setChannels(channelsInfoService.findByIdRe(t.getDefaultChannelsId()));
            	}
            }
            return new ResponseEntity(lst, HttpStatus.OK);
        }
    }

    @Log("分页查询用户信息")
    @ApiOperation(value = "分页查询用户信息")
    @GetMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT')")
    public ResponseEntity getUsers(UserQueryCriteria criteria, Pageable pageable) {
        Set<Long> deptSet = new HashSet<>();

        /**
         * 如果传入了组织，就只当前组织及以下的。
         * 如果没传入组织id，就获取当前用户所在组织下的所有
         */
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
            criteria.setDeptIds(deptSet);
        }else {
            Set<Long> deptIds = dataScope.getDeptIds();
            criteria.setDeptIds(deptIds);
        }
        return new ResponseEntity(userService.queryAll(criteria, pageable), HttpStatus.OK);
    }
    

    @Log("新增用户")
    @ApiOperation(value = "新增用户")
    @PostMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_CREATE')")
    public ResponseEntity create(@Validated @RequestBody User resources) {
        checkLevel(resources);
        UserQueryCriteria criteria=new UserQueryCriteria();
        criteria.setDeptId(resources.getDept().getId());
        DeptDTO dept=deptService.findById(resources.getDept().getId());
        List<UserDTO> deptUsers=userService.queryAll(criteria);
        if(deptUsers!=null && deptUsers.size()>0) {
        	int sum=0;
        	for(UserDTO t:deptUsers) {
        		if(t.getDept()!=null &&( t.getDept().getId().equals(resources.getDept().getId()) || t.getDeptId()==resources.getDept().getId() )) {
        			sum++;
        		}
        	}
        	if(sum>=dept.getMaxPersonNumber()) {
        		throw new BadRequestException("当前组织机构下用户数["+sum+"]已达到账号上限["+dept.getMaxPersonNumber()+"]");
        	}
        }
        return new ResponseEntity(userService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改用户")
    @ApiOperation(value = "修改用户")
    @PutMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_EDIT')")
    public ResponseEntity update(@Validated(User.Update.class) @RequestBody User resources) {
        checkLevel(resources);
        userService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除用户")
    @ApiOperation(value = "删除用户")
    @DeleteMapping(value = "/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        Integer currentLevel = Collections.min(roleService.findByUsers_Id(SecurityUtils.getUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
        Integer optLevel = Collections.min(roleService.findByUsers_Id(id).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));

        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
        userService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 修改密码
     * @param user
     * @return
     */
    @Log("修改密码")
    @ApiOperation(value = "修改密码")
    @PostMapping(value = "/users/updatePass")
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

    /**
     * 修改头像
     *
     * @param file
     * @return
     */
    @Log("修改头像")
    @PostMapping(value = "/users/updateAvatar")
    public ResponseEntity updateAvatar(@RequestParam MultipartFile file) {
        userService.updateAvatar(file);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 修改邮箱
     *
     * @param user
     * @param user
     * @return
     */
    @Log("修改邮箱")
    @PostMapping(value = "/users/updateEmail/{code}")
    public ResponseEntity updateEmail(@PathVariable String code, @RequestBody User user) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        if (!userDetails.getPassword().equals(EncryptUtils.encryptPassword(user.getPassword()))) {
            throw new BadRequestException("密码错误");
        }
        VerificationCode verificationCode = new VerificationCode(code, ElAdminConstant.RESET_MAIL, "email", user.getEmail());
        verificationCodeService.validated(verificationCode);
        userService.updateEmail(userDetails.getUsername(), user.getEmail());
        return new ResponseEntity(HttpStatus.OK);
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
}
