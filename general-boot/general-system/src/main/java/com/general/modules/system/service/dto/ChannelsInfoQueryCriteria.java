package com.general.modules.system.service.dto;

import com.general.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
* @author L
* @date 2019-10-24
*/
@Data
public class ChannelsInfoQueryCriteria{

    @ApiModelProperty(value = "组织ID")
    private Long deptId;

    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private Set<Long> deptIds;

    // 多字段模糊
    @Query(blurry = "channelsName")
    private String blurry;

}