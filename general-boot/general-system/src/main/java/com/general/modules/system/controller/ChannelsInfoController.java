package com.general.modules.system.controller;

import com.general.aop.log.Log;
import com.general.config.DataScope;
import com.general.modules.system.domain.ChannelsInfo;
import com.general.modules.system.service.ChannelsInfoService;
import com.general.modules.system.service.DeptService;
import com.general.modules.system.service.dto.ChannelsInfoQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author L
 * @date 2019-10-24
 */
@Api(tags = "频道管理接口")
@RestController
@RequestMapping("api")
public class ChannelsInfoController {

    @Autowired
    private ChannelsInfoService channelsInfoService;
    @Autowired
    private DataScope dataScope;
    @Autowired
    private DeptService deptService;

    @Log("分页查询频道")
    @ApiOperation(value = "分页查询频道")
    @GetMapping(value = "/channelsInfo")
    @PreAuthorize("hasAnyRole('ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_SELECT')")
    public ResponseEntity getChannelsInfos(ChannelsInfoQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(channelsInfoService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("集合查询频道")
    @ApiOperation(value = "根据组织ID集合查询频道")
    @GetMapping(value = "/listChannelsInfosByDeptId")
    @PreAuthorize("hasAnyRole('ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_SELECT')")
    public ResponseEntity listChannelsInfosByDeptId(ChannelsInfoQueryCriteria criteria) {
        Set<Long> deptSet = new HashSet<>();
        Set<Long> result = new HashSet<>();
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
        }
        // 数据权限
        Set<Long> deptIds = dataScope.getDeptIds();
        // 查询条件不为空并且数据权限不为空则取交集
        result.addAll(deptSet);
        result.addAll(deptIds);
        criteria.setDeptIds(result);
        return new ResponseEntity(channelsInfoService.queryAll(criteria), HttpStatus.OK);
    }

    @Log("新增频道")
    @ApiOperation(value = "新增频道")
    @PostMapping(value = "/channelsInfo")
    @PreAuthorize("hasAnyRole('ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_CREATE')")
    public ResponseEntity create(@Validated @RequestBody ChannelsInfo resources) {
        return new ResponseEntity(channelsInfoService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改频道")
    @ApiOperation(value = "修改频道")
    @PutMapping(value = "/channelsInfo")
    @PreAuthorize("hasAnyRole('ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_EDIT')")
    public ResponseEntity update(@Validated @RequestBody ChannelsInfo resources) {
        channelsInfoService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除频道")
    @ApiOperation(value = "删除频道")
    @DeleteMapping(value = "/channelsInfo/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        channelsInfoService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}