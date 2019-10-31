package com.general.modules.system.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.general.modules.system.domain.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author L
 * @date 2018-11-23
 */
@Data
public class UserDTO implements Serializable {

    @ApiModelProperty(hidden = true)
    private Long id;

    private String username;

    private String avatar;

    private String email;

    private String phone;

    private Boolean enabled;

    @JsonIgnore
    private String password;

    private Timestamp createTime;

    private Date lastPasswordResetTime;

    @ApiModelProperty(hidden = true)
    private Set<RoleSmallDTO> roles;

    @ApiModelProperty(hidden = true)
    private JobSmallDTO job;

    private DeptSmallDTO dept;

    private Long deptId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 频道信息
     */
    private List<ChannelsInfo> channelsList;

    /**
     * 默认频道
     */
    private Long defaultChannelsId;

    /**
     * 企业识别码
     */
    private String enterpriseCode;

    /**
     * 定位开关
     */
    private Integer locationSwitch;

    /**
     * 定位间隔
     */
    private Integer locationInterval;

    /**
     * 视频功能开关
     */
    private Integer videoSwitch;

    /**
     * 服务期限
     */
    private String serviceTime;

    private Long createUser;

    private Date updateTime;

    private Long updateUser;
}
