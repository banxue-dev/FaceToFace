package com.general.modules.system.controller;

import com.general.config.DataScope;
import com.general.modules.system.service.ChannelsInfoService;
import com.general.modules.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @Description:
 * @Author LuoJing
 * @Date 2019/12/1810:59
 */
@Api(tags = "首页接口")
@RestController
@RequestMapping("api")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private DataScope dataScope;

    @Autowired
    private ChannelsInfoService channelsInfoService;

    @ApiOperation(value = "用户七天内的新增数量")
    @GetMapping(value = "/home/getUserChartData")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL')")
    public ResponseEntity getUserChartData() {
        // 数据权限
        Set<Long> deptIds = dataScope.getDeptIds();
        return new ResponseEntity(userService.getUserChartData(deptIds), HttpStatus.OK);
    }

    @ApiOperation(value = "用户七天内的频道新增数量")
    @GetMapping(value = "/home/getChannelsChartData")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL')")
    public ResponseEntity getChannelsChartData() {
        // 数据权限
        Set<Long> deptIds = dataScope.getDeptIds();
        return new ResponseEntity(channelsInfoService.getChannelsChartData(deptIds), HttpStatus.OK);
    }

    @ApiOperation(value = "统计七天的数据")
    @GetMapping(value = "/home/getUserChannelsStatistics")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL')")
    public ResponseEntity getUserChannelsStatistics() {
        // 数据权限
        Set<Long> deptIds = dataScope.getDeptIds();
        return new ResponseEntity(userService.getUserChannelsStatistics(deptIds), HttpStatus.OK);
    }
}
