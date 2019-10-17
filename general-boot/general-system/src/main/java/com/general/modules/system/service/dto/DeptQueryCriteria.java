package com.general.modules.system.service.dto;

import com.general.annotation.Query;
import lombok.Data;

import java.util.Set;

/**
* @author L
* @date 2019-03-25
*/
@Data
public class DeptQueryCriteria{

    @Query(type = Query.Type.IN, propName="id")
    private Set<Long> ids;

    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query
    private Boolean enabled;

    @Query
    private Long pid;
}