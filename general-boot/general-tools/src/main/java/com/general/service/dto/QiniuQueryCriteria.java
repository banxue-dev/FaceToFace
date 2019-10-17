package com.general.service.dto;

import com.general.annotation.Query;
import lombok.Data;

/**
 * @author L
 * @date 2019-6-4 09:54:37
 */
@Data
public class QiniuQueryCriteria{

    @Query(type = Query.Type.INNER_LIKE)
    private String key;
}
