package com.general.modules.system.service.dto;

import com.general.modules.system.domain.Dept;
import com.general.modules.system.domain.User;
import lombok.Data;

import java.sql.Timestamp;
import java.io.Serializable;
import java.util.Set;


/**
 * @author L
 * @date 2019-10-24
 */
@Data
public class ChannelsInfoDTO implements Serializable {

    private Long id;

    // 频道属性（0音频，1音视频）
    private Integer attr;

    // 创建时间
    private Timestamp createTime;

    // 创建用户
    private Long createUser;

    // 频道内最大人数
    private Integer maxPersonNumber;

    // 频道模式（0申请发言，1自由发言，2静默）
    private Integer mode;

    // 频道名
    private String channelsName;

    // 频道录音开关(1开，0关)
    private Boolean recordSwitch;

    // 修改时间
    private Timestamp updateTime;

    // 修改用户
    private Long updateUser;

    // 组织ID
    private Dept dept;

    // 普通用户
    private Set<User> userSet;

    //管理员用户
    private Set<User> userAdmin;
}