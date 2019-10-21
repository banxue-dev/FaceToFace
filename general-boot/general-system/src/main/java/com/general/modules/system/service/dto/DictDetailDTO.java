package com.general.modules.system.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @author L
* @date 2019-04-10
*/
@Data
public class DictDetailDTO implements Serializable {

    private Long id;

    /**
     * 字典标签
     */
    private String label;

    /**
     * 字典值
     */
    private String value;

    /**
     * 排序
     */
    private String sort;
}