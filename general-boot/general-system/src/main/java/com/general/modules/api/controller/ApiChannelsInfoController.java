package com.general.modules.api.controller;

import com.general.aop.log.Log;
import com.general.config.DataScope;
import com.general.modules.system.domain.ChannelsInfo;
import com.general.modules.system.service.ChannelsInfoService;
import com.general.modules.system.service.DeptService;
import com.general.modules.system.service.dto.ChannelsInfoQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author L
 * @date 2019-10-24
 */
@Api(tags = "外部接口-频道管理接口")
@RestController
@RequestMapping("pass")
public class ApiChannelsInfoController {

    @Autowired
    private ChannelsInfoService channelsInfoService;

    @Log("状态管理器-根据频道ID查询")
    @ApiOperation(value = "状态管理器-根据频道ID查询")
    @GetMapping(value = "/getChannelsInfosById/{id}")
    public ResponseEntity getChannelsInfosById(@PathVariable Long id) {
        return new ResponseEntity(channelsInfoService.findById(id), HttpStatus.OK);
    }

    @Log("状态管理器-根据频道ID查询")
    @ApiOperation(value = "状态管理器-根据频道ID查询")
    @GetMapping(value = "/getChannelsInfosByUserId/{userId}")
    public ResponseEntity getChannelsInfosByUserId(@PathVariable Long userId) {
        return new ResponseEntity(channelsInfoService.getAppUserChannelsInfo(userId), HttpStatus.OK);
    }

    @Log("状态管理器-查询所有频道")
    @ApiOperation(value = "状态管理器-查询所有频道")
    @GetMapping(value = "/listChannelsInfos")
    public ResponseEntity listChannelsInfos() {
        return new ResponseEntity(channelsInfoService.queryAll(null), HttpStatus.OK);
    }

    @Log("平台客户端-新增频道")
    @ApiOperation(value = "状态管理器-新增频道")
    @PostMapping(value = "/channelsInfo")
//    @PreAuthorize("hasAnyRole('ADMIN','CHANNELSINFO_ALL','CHANNELSINFO_CREATE')")
    public ResponseEntity create(@Validated @RequestBody ChannelsInfo resources) {
        return new ResponseEntity(channelsInfoService.create(resources), HttpStatus.CREATED);
    }

    @Log("平台客户端-修改频道")
    @ApiOperation(value = "状态管理器-修改频道")
    @PutMapping(value = "/channelsInfo")
    public ResponseEntity update(@Validated @RequestBody ChannelsInfo resources) {
        channelsInfoService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("平台客户端-删除频道")
    @ApiOperation(value = "状态管理器-删除频道")
    @DeleteMapping(value = "/channelsInfo/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        channelsInfoService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
