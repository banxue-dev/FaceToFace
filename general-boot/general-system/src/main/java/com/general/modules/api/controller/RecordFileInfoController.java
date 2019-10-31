package com.general.modules.api.controller;

import com.general.aop.log.Log;
import com.general.modules.api.domain.RecordFileInfo;
import com.general.modules.api.service.RecordFileInfoService;
import com.general.modules.api.service.dto.RecordFileInfoQueryCriteria;
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
 * @date 2019-10-28
 */
@Api(tags = "录音文件接口")
@RestController
@RequestMapping("pass")
public class RecordFileInfoController {

    @Autowired
    private RecordFileInfoService recordFileInfoService;

    @Log("查询录音文件")
    @ApiOperation(value = "查询录音文件")
    @GetMapping(value = "/recordFileInfo")
//    @PreAuthorize("hasAnyRole('ADMIN','RECORDFILEINFO_ALL','RECORDFILEINFO_SELECT')")
    public ResponseEntity getRecordFileInfos(RecordFileInfoQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity(recordFileInfoService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增录音文件")
    @ApiOperation(value = "新增录音文件")
    @PostMapping(value = "/recordFileInfo")
//    @PreAuthorize("hasAnyRole('ADMIN','RECORDFILEINFO_ALL','RECORDFILEINFO_CREATE')")
    public ResponseEntity create(@Validated @RequestBody RecordFileInfo resources) {
        return new ResponseEntity(recordFileInfoService.create(resources), HttpStatus.CREATED);
    }

//    @Log("修改录音文件")
//    @ApiOperation(value = "修改录音文件")
//    @PutMapping(value = "/recordFileInfo")
//    @PreAuthorize("hasAnyRole('ADMIN','RECORDFILEINFO_ALL','RECORDFILEINFO_EDIT')")
    public ResponseEntity update(@Validated @RequestBody RecordFileInfo resources) {
        recordFileInfoService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @Log("删除录音文件")
//    @ApiOperation(value = "删除录音文件")
//    @DeleteMapping(value = "/recordFileInfo/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN','RECORDFILEINFO_ALL','RECORDFILEINFO_DELETE')")
    public ResponseEntity delete(@PathVariable Long id) {
        recordFileInfoService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}