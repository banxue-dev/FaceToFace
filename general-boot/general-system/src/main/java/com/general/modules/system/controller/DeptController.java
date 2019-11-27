package com.general.modules.system.controller;

import com.general.aop.log.Log;
import com.general.config.DataScope;
import com.general.exception.BadRequestException;
import com.general.modules.system.domain.Dept;
import com.general.modules.system.service.DeptService;
import com.general.modules.system.service.dto.DeptDTO;
import com.general.modules.system.service.dto.DeptQueryCriteria;
import com.general.utils.ThrowableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author L
* @date 2019-03-25
*/
@RestController
@RequestMapping("api")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @Autowired
    private DataScope dataScope;

    private static final String ENTITY_NAME = "dept";

    @Log("查询组织")
    @GetMapping(value = "/dept")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT','DEPT_ALL','DEPT_SELECT')")
    public ResponseEntity getDepts(DeptQueryCriteria criteria){
        // 数据权限
//        criteria.setIds(dataScope.getDeptIds());
//        List<DeptDTO> deptDTOS = deptService.queryAll(criteria);
//        return new ResponseEntity(deptService.buildTree(deptDTOS),HttpStatus.OK);
    	Set<DeptDTO> s=dataScope.getDeptDTOS();
    	Map map = new HashMap();
    	map.put("totalElements",s==null?0:s.size());
    	map.put("content",s);
        return new ResponseEntity(map,HttpStatus.OK);
    }

    @Log("新增组织")
    @PostMapping(value = "/dept")
    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Dept resources){
        if (resources.getId() != null) {
            throw new BadRequestException("新的组织 "+ ENTITY_NAME +" 存在组织ID");
        }
        return new ResponseEntity(deptService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改组织")
    @PutMapping(value = "/dept")
    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_EDIT')")
    public ResponseEntity update(@Validated(Dept.Update.class) @RequestBody Dept resources){
        deptService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除组织")
    @DeleteMapping(value = "/dept/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        try {
            deptService.delete(id);
        }catch (Throwable e){
            ThrowableUtil.throwForeignKeyException(e, "该组织存在岗位或者角色关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}