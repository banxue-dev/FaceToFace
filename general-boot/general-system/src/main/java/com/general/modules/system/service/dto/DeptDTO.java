package com.general.modules.system.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;

/**
* @author L
* @date 2019-03-25
*/
@Data
public class DeptDTO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    @NotNull
    private Boolean enabled;

    /**
     * 上级组织
     */
    private Long pid;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptDTO> children;

    private Timestamp createTime;

    /**
     * 当前组织内最大人数
     */
    private Integer maxPersonNumber;
    /**
     * 子组织内最大人数
     */
//    private Integer childMaxPersonNumber;

    /**
     * 企业识别码
     */
    private String enterpriseCode;

    public String getLabel() {
        return name;
    }
}