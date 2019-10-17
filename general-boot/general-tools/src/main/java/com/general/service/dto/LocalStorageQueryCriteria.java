package com.general.service.dto;

import com.general.annotation.Query;
import lombok.Data;

/**
* @author L
* @date 2019-09-05
*/
@Data
public class LocalStorageQueryCriteria{

    // 模糊
    @Query(blurry = "name,suffix,type,operate,size")
    private String blurry;
}