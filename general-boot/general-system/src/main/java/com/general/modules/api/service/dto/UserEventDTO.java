package com.general.modules.api.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author L
* @date 2019-10-28
*/
@Data
public class UserEventDTO implements Serializable {

    private Long id;

    // 事件：0上线，1下线
    private Integer event;

    // 时间
    private Timestamp time;

    // 账号ID（用户ID）
    private Long userId;
}