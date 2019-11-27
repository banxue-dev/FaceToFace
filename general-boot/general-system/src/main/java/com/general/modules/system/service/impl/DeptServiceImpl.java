package com.general.modules.system.service.impl;

import com.general.exception.BadRequestException;
import com.general.modules.system.domain.Dept;
import com.general.modules.system.service.DeptService;
import com.general.modules.system.service.mapper.DeptMapper;
import com.general.utils.QueryHelp;
import com.general.utils.ValidationUtil;
import com.general.modules.system.service.dto.DeptQueryCriteria;
import com.general.modules.system.repository.DeptRepository;
import com.general.modules.system.service.dto.DeptDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author L
* @date 2019-03-25
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<DeptDTO> queryAll(DeptQueryCriteria criteria) {
        return deptMapper.toDto(deptRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public DeptDTO findById(Long id) {
        Optional<Dept> dept = deptRepository.findById(id);
        ValidationUtil.isNull(dept,"Dept","id",id);
        return deptMapper.toDto(dept.get());
    }

    @Override
    public List<Dept> findByPid(long pid) {
        return deptRepository.findByPid(pid);
    }

    @Override
    public Set<Dept> findByRoleIds(Long id) {
        return deptRepository.findByRoles_Id(id);
    }

    List<Long> haveIds=new ArrayList<Long>();

    @Override
    public Object buildTree(List<DeptDTO> deptDTOS) {
    	Set<DeptDTO> trees = new LinkedHashSet<>();
    	Set<DeptDTO> depts= new LinkedHashSet<>();
    	List<String> deptNames = deptDTOS.stream().map(DeptDTO::getName).collect(Collectors.toList());
    	Boolean isChild;
    	for (DeptDTO deptDTO : deptDTOS) {
    		isChild = false;
    		if ("0".equals(deptDTO.getPid().toString())) {
    			trees.add(deptDTO);
    		}
    		for (DeptDTO it : deptDTOS) {
    			if (it.getPid().equals(deptDTO.getId())) {
    				isChild = true;
    				if (deptDTO.getChildren() == null) {
    					deptDTO.setChildren(new ArrayList<DeptDTO>());
    				}
    				deptDTO.getChildren().add(it);
    			}
    		}
    		if(isChild)
    			if(!depts.contains(deptDTO)) {
    				depts.add(deptDTO);
    			}
    			else if(!deptNames.contains(deptRepository.findNameById(deptDTO.getPid())))
    				depts.add(deptDTO);
    	}
    	
    	if (CollectionUtils.isEmpty(trees)) {
    		trees = depts;
    	}
    	
    	Integer totalElements = deptDTOS!=null?deptDTOS.size():0;
    	
    	Map map = new HashMap();
    	map.put("totalElements",totalElements);
    	map.put("content",CollectionUtils.isEmpty(trees)?deptDTOS:trees);
    	return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeptDTO create(Dept resources) {
        return deptMapper.toDto(deptRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Dept resources) {
        if(resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Optional<Dept> optionalDept = deptRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalDept,"Dept","id",resources.getId());
        Dept dept = optionalDept.get();
        resources.setId(dept.getId());
        deptRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        deptRepository.deleteById(id);
    }
}