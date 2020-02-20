package com.general.modules.api.controller;

import com.general.aop.log.Log;
import com.general.modules.system.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "外部接口-组织管理接口")
@RestController
@RequestMapping("pass")
public class ApiDeptController {

    @Autowired
    private DeptService deptService;

    @Log("状态管理器-根据组织ID查询")
    @ApiOperation(value = "状态管理器-根据组织ID查询")
    @GetMapping(value = "/getDeptById/{id}")
    public ResponseEntity getDeptById(@PathVariable Long id) {
        return new ResponseEntity(deptService.findById(id), HttpStatus.OK);
    }
}
