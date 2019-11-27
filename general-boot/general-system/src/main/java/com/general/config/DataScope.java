package com.general.config;

import com.general.modules.system.domain.Dept;
import com.general.modules.system.service.DeptService;
import com.general.modules.system.service.RoleService;
import com.general.modules.system.service.UserService;
import com.general.modules.system.service.dto.DeptDTO;
import com.general.modules.system.service.dto.DeptQueryCriteria;
import com.general.modules.system.service.dto.RoleSmallDTO;
import com.general.modules.system.service.dto.UserDTO;
import com.general.utils.SecurityUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据权限配置
 * @author L
 * @date 2019-4-1
 */
@Component
public class DataScope {

    private final String[] scopeType = {"全部","本级","自定义"};

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;

    public Set<Long> getDeptIds() {

        UserDTO user = userService.findByName(SecurityUtils.getUsername());

        // 用于存储组织id
        Set<Long> deptIds = new HashSet<>();

        // 查询用户角色
        List<RoleSmallDTO> roleSet = roleService.findByUsers_Id(user.getId());

        for (RoleSmallDTO role : roleSet) {

            if (scopeType[0].equals(role.getDataScope())) {
                return new HashSet<>() ;
            }

            // 存储本级的数据权限
            if (scopeType[1].equals(role.getDataScope())) {
                deptIds.add(user.getDept().getId());
            }

            // 存储自定义的数据权限
            if (scopeType[2].equals(role.getDataScope())) {
                Set<Dept> depts = deptService.findByRoleIds(role.getId());
                for (Dept dept : depts) {
                    deptIds.add(dept.getId());
                    List<Dept> deptChildren = deptService.findByPid(dept.getId());
                    if (deptChildren != null && deptChildren.size() != 0) {
                    	List<Long> list=getDeptChildren(deptChildren);
                    	for(Long l:list) {
                    		if(!deptIds.contains(l)) {
                    			deptIds.add(l);
                    		}
                    	}
                    }
                }
            }
        }
        return deptIds;
    }
    /**
     * 这个方法直接返回所有的组织父子对象
     * @return
     * 2019年11月27日
     * fengchaseyou
     */
    public Set<DeptDTO> getDeptDTOS() {

        UserDTO user = userService.findByName(SecurityUtils.getUsername());

        // 用于存储组织id
        Set<DeptDTO> deptDTOS = new HashSet<DeptDTO>();

        // 查询用户角色
        List<RoleSmallDTO> roleSet = roleService.findByUsers_Id(user.getId());

        for (RoleSmallDTO role : roleSet) {

            if (scopeType[0].equals(role.getDataScope())) {
                return new HashSet<>() ;
            }

            // 存储本级的数据权限
            if (scopeType[1].equals(role.getDataScope())) {
            	DeptDTO n=new DeptDTO();
            	BeanUtils.copyProperties(user.getDept(), n);
            	deptDTOS.add(n);
            }

            // 存储自定义的数据权限
            if (scopeType[2].equals(role.getDataScope())) {
                Set<Dept> depts = deptService.findByRoleIds(role.getId());
                DeptDTO n=new DeptDTO();
                for (Dept dept : depts) {
                	BeanUtils.copyProperties(dept, n);
                	this.getDeptChildrenBy(n);
                	deptDTOS.add(n);
                }
            }
        }
        return deptDTOS;
    }

    /**
     * 获取某个组织的所有下级,一直下钻到没有下级为止
     * @param dept
     * 2019年11月27日
     * fengchaseyou
     */
    public void getDeptChildrenBy(DeptDTO dept) {
    	DeptQueryCriteria criteria=new DeptQueryCriteria();
    	criteria.setPid(dept.getId());
        List<DeptDTO> deptChildren = deptService.queryAll(criteria);
        if(deptChildren!=null && deptChildren.size()>0) {
        	/**
        	 * 如果有下级,就去看看下级是不是还有下级
        	 */
        	for(DeptDTO c:deptChildren) {
        		this.getDeptChildrenBy(c);
        	}
        	dept.setChildren(deptChildren);
        }
    }


    public List<Long> getDeptChildren(List<Dept> deptList) {
        List<Long> list = new ArrayList<>();
        deptList.forEach(dept -> {
                    if (dept!=null && dept.getEnabled()){
                        List<Dept> depts = deptService.findByPid(dept.getId());
                        if(deptList!=null && deptList.size()!=0){
                        	List<Long> temp=getDeptChildren(depts);
                        	for(Long l:temp) {
                        		if(!list.contains(l)) {
                        			list.add(l);
                        		}
                        	}
                        }
                        if(!list.contains(dept.getId())) {
                			list.add(dept.getId());
                		}
                    }
                }
        );
        return list;
    }
    public static void main(String[] args) {
    	Pattern p = Pattern.compile("([0-9A-Za-z]*-([S]?|[0-9]){3,4}-[0-9]{2,3})");// 匹配票据编号正则表达式
    	String result=" {800CW00191100244-003-01,Picture recognize failed!}";
    	Matcher m = p.matcher(result);
		if (m.find()) {
			System.out.println(m.group());
		}else {
			System.out.println("没得");
		}
	}
}
