package com.general.modules.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.general.config.DataScope;
import com.general.exception.BadRequestException;
import com.general.modules.system.domain.Dept;
import com.general.modules.system.service.DeptService;
import com.general.modules.system.service.UserService;
import com.general.modules.system.service.dto.DeptDTO;
import com.general.modules.system.service.dto.DeptQueryCriteria;
import com.general.modules.system.service.dto.UserDTO;
import com.general.modules.system.service.dto.UserQueryCriteria;
import com.general.utils.SecurityUtils;
import com.general.utils.ThrowableUtil;

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
    
    @Autowired
    private UserService userService;

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
        if (resources.getMaxPersonNumber() == null || resources.getMaxPersonNumber()<1) {
        	throw new BadRequestException("账号人数不能为空,且必须大于等于0");
        }
        if(resources.getPid()==-1) {
        	/*
        	 * 
        	 * -1表示没选择,就用当前用户的id
        	 * 表示是自己下面的,因为用户添加只能添加自己的下级
        	 */
        	UserDTO user = userService.findByName(SecurityUtils.getUsername());
        	resources.setPid(user.getDept().getId());
        }
        /**
         * 拿到父节点
         */
        DeptDTO parentDept=deptService.findById(resources.getPid());
    	DeptQueryCriteria criteria=new DeptQueryCriteria();
        Integer sumCount=0;
        if(parentDept.getPid()==0) {
        	/**
        	 * 自己已经是根节点了
        	 * 拿到自己的所有子节点
        	 */
        	criteria.setPid(resources.getId());
            List<DeptDTO> allChildren = deptService.queryAll(criteria);
            if(allChildren!=null && allChildren.size()>0) {
            	for(DeptDTO temp:allChildren) {
            		sumCount+=temp.getMaxPersonNumber();
            	}
            }
            if(sumCount>resources.getChildMaxPersonNumber()) {
            	throw new BadRequestException("账号上限不足子节点人数总合,目前总共有:"+(sumCount));
            }
        }else {
        	/**
        	 * 拿到这个父级节点和他的子节点
        	 */
        	criteria.setPid(resources.getPid());
            List<DeptDTO> allChildren = deptService.queryAll(criteria);
            if(allChildren!=null && allChildren.size()>0) {
            	for(DeptDTO temp:allChildren) {
            		sumCount+=temp.getMaxPersonNumber();
            	}
            }
            if(sumCount+resources.getMaxPersonNumber()>parentDept.getChildMaxPersonNumber()) {
            	throw new BadRequestException("账号上限超过父节点设置数据,目前还剩余:"+(parentDept.getChildMaxPersonNumber()-sumCount));
            }
        }
        
        return new ResponseEntity(deptService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改组织")
    @PutMapping(value = "/dept")
    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_EDIT')")
    public ResponseEntity update(@Validated(Dept.Update.class) @RequestBody Dept resources){
    	if (resources.getMaxPersonNumber() == null || resources.getMaxPersonNumber()<0) {
        	throw new BadRequestException("账号人数不能为空,且必须大于等于0");
        }
        /**
    	 * 拿到这个父级节点和他的子节点
    	 */
    	DeptQueryCriteria criteria=new DeptQueryCriteria();

        Integer sumCount=0;
        DeptDTO parentDept=deptService.findById(resources.getPid());
        if(parentDept.getPid()==0) {
        	/**
        	 * 自己已经是根节点了
        	 * 拿到自己的所有子节点
        	 */
        	criteria.setPid(resources.getId());
            List<DeptDTO> allChildren = deptService.queryAll(criteria);
            if(allChildren!=null && allChildren.size()>0) {
            	for(DeptDTO temp:allChildren) {
            		sumCount+=temp.getMaxPersonNumber();
            	}
            }
            if(sumCount>resources.getChildMaxPersonNumber()) {
            	throw new BadRequestException("账号上限不足子节点人数总合,目前总共有:"+(sumCount));
            }
        }else {

        	criteria.setPid(resources.getPid());
            List<DeptDTO> allChildren = deptService.queryAll(criteria);
        	 /**
             * 拿到旧节点
             */
            DeptDTO old=deptService.findById(resources.getId());
            if(allChildren!=null && allChildren.size()>0) {
            	for(DeptDTO temp:allChildren) {
            		sumCount+=temp.getMaxPersonNumber();
            	}
            }
            /**
             * 排除掉老的
             */
            sumCount-=old.getMaxPersonNumber();
            if(sumCount+resources.getMaxPersonNumber()>parentDept.getChildMaxPersonNumber()) {
            	throw new BadRequestException("账号上限超过父节点设置数据,目前还剩余:"+(parentDept.getChildMaxPersonNumber()-sumCount));
            }
            UserQueryCriteria criteria1=new UserQueryCriteria();
            criteria1.setDeptId(resources.getId());
            List<UserDTO> deptUsers=userService.queryAll(criteria1);
            if(deptUsers!=null && deptUsers.size()>0) {
            	int sum=0;
            	for(UserDTO t:deptUsers) {
            		if(t.getDept()!=null && ( t.getDept().getId().equals(resources.getId()) || t.getDeptId()==resources.getId() )) {
            			sum++;
            		}
            	}
            	if(sum>resources.getChildMaxPersonNumber()) {
            		throw new BadRequestException("当前组织机构下已有"+sum+"个用户,请将账号上限大于此人数");
            	}
            }
        }
       
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