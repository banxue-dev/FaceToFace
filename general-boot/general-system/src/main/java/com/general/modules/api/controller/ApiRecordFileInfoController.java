package com.general.modules.api.controller;

import com.general.aop.log.Log;
import com.general.common.Result;
import com.general.modules.api.domain.RecordFileInfo;
import com.general.modules.api.service.RecordFileInfoService;
import com.general.modules.api.service.dto.RecordFileInfoDTO;
import com.general.modules.api.service.dto.RecordFileInfoQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.Map;

/**
 * @author L
 * @date 2019-10-28
 */
@Api(tags = "录音文件接口")
@RestController
@RequestMapping("pass")
public class ApiRecordFileInfoController {

    @Autowired
    private RecordFileInfoService recordFileInfoService;

    @Log("查询录音文件")
    @ApiOperation(value = "查询录音文件")
    @GetMapping(value = "/recordFileInfo")
    public Result getRecordFileInfos(RecordFileInfoQueryCriteria criteria, Pageable pageable) {
        Map<String,Object> map = recordFileInfoService.queryAll(criteria, pageable);
        return new Result(map);
    }

    @Log("新增录音文件")
    @ApiOperation(value = "新增录音文件")
    @PostMapping(value = "/recordFileInfo")
    public Result create(@Validated @RequestBody RecordFileInfo resources) {
        RecordFileInfoDTO recordFileInfoDTO =  recordFileInfoService.create(resources);
        return new Result(recordFileInfoDTO);
    }
}