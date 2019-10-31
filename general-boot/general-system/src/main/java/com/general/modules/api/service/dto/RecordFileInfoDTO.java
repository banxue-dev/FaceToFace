package com.general.modules.api.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author L
* @date 2019-10-28
*/
@Data
public class RecordFileInfoDTO implements Serializable {

    private Long id;

    // 文件开始事件
    private Timestamp beginTime;

    // 频道ID
    private Long channelsId;

    // 文件结束事件
    private Timestamp endTime;

    // 文件名
    private String fileName;
}