package com.general.modules.system.service.dto;

import com.general.annotation.Query;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author L
 * @date 2018-11-23
 */
@Data
public class UserQueryCriteria implements Serializable {

    @Query
    private Long id;

    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private Set<Long> deptIds;

    // 多字段模糊
    @Query(blurry = "email,username")
    private String blurry;

    @Query
    private Boolean enabled;

    private Long deptId;

}
