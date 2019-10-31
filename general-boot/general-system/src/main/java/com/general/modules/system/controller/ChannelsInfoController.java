package com.general.modules.system.controller;

import com.general.aop.log.Log;
import com.general.modules.system.domain.ChannelsInfo;
import com.general.modules.system.service.ChannelsInfoService;
import com.general.modules.system.service.dto.ChannelsInfoQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

/**
* @author L
* @date 2019-10-24
*/
@Api(tags = "频道管理")
@RestController
@RequestMapping("api")
public class ChannelsInfoController {

    @Autowired
    private ChannelsInfoService channelsInfoService;

    @Log("查询频道")
    @ApiOperation(value = "查询频道")
    @GetMapping(value = "/channelsInfo")
    @PreAuthorize("hasAnyRole('ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_SELECT')")
    public ResponseEntity getChannelsInfos(ChannelsInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(channelsInfoService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增频道")
    @ApiOperation(value = "新增频道")
    @PostMapping(value = "/channelsInfo")
    @PreAuthorize("hasAnyRole('ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_CREATE')")
    public ResponseEntity create(@Validated @RequestBody ChannelsInfo resources){
        return new ResponseEntity(channelsInfoService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改频道")
    @ApiOperation(value = "修改频道")
    @PutMapping(value = "/channelsInfo")
    @PreAuthorize("hasAnyRole('ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_EDIT')")
    public ResponseEntity update(@Validated @RequestBody ChannelsInfo resources){
        channelsInfoService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除频道")
    @ApiOperation(value = "删除频道")
    @DeleteMapping(value = "/channelsInfo/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        channelsInfoService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}