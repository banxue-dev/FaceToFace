package com.general.modules.api.controller;

import com.general.aop.log.Log;
import com.general.modules.api.domain.UserEvent;
import com.general.modules.api.service.UserEventService;
import com.general.modules.api.service.dto.UserEventQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

/**
 * @author L
 * @date 2019-10-28
 */
@Api(tags = "账号事件接口")
@RestController
@RequestMapping("pass")
public class UserEventController {

    @Autowired
    private UserEventService userEventService;

    @Log("查询账号事件")
    @ApiOperation(value = "查询账号事件")
    @GetMapping(value = "/userEvent")
//    @PreAuthorize("hasAnyRole('ADMIN','USEREVENT_ALL','USEREVENT_SELECT')")
    public ResponseEntity getUserEvents(UserEventQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(userEventService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增账号事件")
    @ApiOperation(value = "新增账号事件")
    @PostMapping(value = "/userEvent")
//    @PreAuthorize("hasAnyRole('ADMIN','USEREVENT_ALL','USEREVENT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody UserEvent resources) {
        return new ResponseEntity(userEventService.create(resources), HttpStatus.CREATED);
    }

//    @Log("修改账号事件")
//    @ApiOperation(value = "修改账号事件")
//    @PutMapping(value = "/userEvent")
//    @PreAuthorize("hasAnyRole('ADMIN','USEREVENT_ALL','USEREVENT_EDIT')")
    public ResponseEntity update(@Validated @RequestBody UserEvent resources) {
        userEventService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @Log("删除账号事件")
//    @ApiOperation(value = "删除账号事件")
//    @DeleteMapping(value = "/userEvent/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN','USEREVENT_ALL','USEREVENT_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        userEventService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}