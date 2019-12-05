package com.general.modules.system.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.general.aop.log.Log;
import com.general.exception.BadRequestException;
import com.general.modules.system.domain.Role;
import com.general.modules.system.service.RoleService;
import com.general.modules.system.service.dto.RoleDTO;
import com.general.modules.system.service.dto.RoleQueryCriteria;
import com.general.modules.system.service.dto.RoleSmallDTO;
import com.general.utils.SecurityUtils;
import com.general.utils.ThrowableUtil;

import cn.hutool.core.lang.Dict;

/**
 * @author L
 * @date 2018-12-03
 */
@RestController
@RequestMapping("api")
public class RoleController {

    @Autowired
    private RoleService roleService;

    private static final String ENTITY_NAME = "role";

    /**
     * 获取单个role
     * @param id
     * @return
     */
    @GetMapping(value = "/roles/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_SELECT')")
    public ResponseEntity getRoles(@PathVariable Long id){
        return new ResponseEntity(roleService.findById(id), HttpStatus.OK);
    }

    /**
     * 返回全部的角色，新增用户时下拉选择
     * @return
     */
    @GetMapping(value = "/roles/all")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','USER_ALL','USER_CREATE','USER_EDIT')")
    public ResponseEntity getAll(@PageableDefault(value = 2000, sort = {"level"}, direction = Sort.Direction.ASC) Pageable pageable){
    	List<RoleDTO> lst=roleService.queryAll(pageable);
    	List<RoleDTO> nlst=new ArrayList<RoleDTO>();
    	for(RoleDTO r:lst) {
    		if(r.getId()!=1) {
    			nlst.add(r);
    		}
    	}
    	return new ResponseEntity(nlst,HttpStatus.OK);
    }
    /**
     * 列表数据
     * @param criteria
     * @param pageable
     * @return
     * 2019年12月3日
     * fengchaseyou
     */
    @Log("查询角色|列表")
    @GetMapping(value = "/roles")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_SELECT')")
    public ResponseEntity getRoles(RoleQueryCriteria criteria, Pageable pageable){
    	criteria.setNotEq(2);
    	Page<RoleDTO> page= roleService.queryAll(criteria,pageable);
//    	List<RoleDTO> lst=page.getContent();
//    	List<RoleDTO> rs=new ArrayList<RoleDTO>();
//    	for(RoleDTO r:lst) {
//    		if(r.getId()==1) {
//    			rs.add(r);
//    		}
//    	}
//    	if(rs.size()>0) {
//    		
//    		lst.removeAll(rs);
//    	}
        return new ResponseEntity(page,HttpStatus.OK);
    }

    @GetMapping(value = "/roles/level")
    public ResponseEntity getLevel(){
        List<Integer> levels = roleService.findByUsers_Id(SecurityUtils.getUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList());
        return new ResponseEntity(Dict.create().set("level", Collections.min(levels)),HttpStatus.OK);
    }

    @Log("新增角色")
    @PostMapping(value = "/roles")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Role resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity(roleService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改角色")
    @PutMapping(value = "/roles")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public ResponseEntity update(@Validated(Role.Update.class) @RequestBody Role resources){
        roleService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("修改角色权限")
    @PutMapping(value = "/roles/permission")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public ResponseEntity updatePermission(@RequestBody Role resources){
        roleService.updatePermission(resources,roleService.findById(resources.getId()));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("修改角色菜单")
    @PutMapping(value = "/roles/menu")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public ResponseEntity updateMenu(@RequestBody Role resources){
        roleService.updateMenu(resources,roleService.findById(resources.getId()));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除角色")
    @DeleteMapping(value = "/roles/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        try {
            roleService.delete(id);
        }catch (Throwable e){
            ThrowableUtil.throwForeignKeyException(e, "该角色存在用户关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
