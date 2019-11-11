package com.general.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author L
 * @date 2018-11-22
 */
@Entity
@Getter
@Setter
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    private Long id;
    /**
     * 登录名
     */
    @NotBlank
    @Column(name = "username", unique = true, columnDefinition = "varchar(20) COMMENT '登录名'")
    private String username;

    @Column(name = "password", columnDefinition = "varchar(20) COMMENT '登录密码'")
    private String password;

    /**
     * 用户名
     */
    @Column(name = "name", columnDefinition = "varchar(50) COMMENT '用户名'")
    private String name;

    @OneToOne
    @JoinColumn(name = "avatar_id", columnDefinition = "varchar(50) COMMENT '用户头像'")
    private UserAvatar userAvatar;

    @Column(name = "email", columnDefinition = "varchar(50) COMMENT '用户邮箱'")
    @Pattern(regexp = "([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}", message = "格式错误")
    private String email;

    @Column(name = "phone", columnDefinition = "varchar(20) COMMENT '电话号码'")
    private String phone;

    /**
     * 启停开关（激活账号或停用账号）
     */
    @NotNull
    @Column(name = "enabled", columnDefinition = "varchar(2) COMMENT '启停开关（激活账号或停用账号）状态：1启用、0禁用'")
    private Boolean enabled;

    @Column(name = "last_password_reset_time", columnDefinition = "varchar(20) COMMENT '最后修改密码的日期")
    private Date lastPasswordResetTime;

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles;

    @OneToOne
    @JoinColumn(name = "dept_id", columnDefinition = "varchar(20) COMMENT '组织ID'")
    private Dept dept;

    /**
     * 等级
     */
    @Column(name = "level", columnDefinition = "int(10) COMMENT '等级'")
    private Integer level;

    /**
     * 企业识别码
     */
    @Column(name = "enterprise_code", columnDefinition = "varchar(50) COMMENT '组织ID'")
    private String enterpriseCode;

    /**
     * 定位开关
     */
    @Column(name = "location_switch", columnDefinition = "varchar(2) COMMENT '定位开关(1开，0关)'")
    private Boolean locationSwitch;

    /**
     * 定位间隔
     */
    @Column(name = "location_interval", columnDefinition = "varchar(5) COMMENT '定位间隔(单位分钟)'")
    private String locationInterval;

    /**
     * 视频功能开关
     */
    @Column(name = "video_switch", columnDefinition = "varchar(2) COMMENT '视频功能开关(1开，0关)'")
    private Boolean videoSwitch;

    /**
     * 服务期限
     */
    @Column(name = "service_time", columnDefinition = "varchar(20) COMMENT '服务期限'")
    private String serviceTime;

    @CreationTimestamp
    @Column(name = "create_time", columnDefinition = "datetime COMMENT '创建时间'")
    private Date createTime;

    @Column(name = "create_user", columnDefinition = "bigint(20) COMMENT '创建用户'")
    private Long createUser;

    @UpdateTimestamp
    @Column(name = "update_time", columnDefinition = "datetime COMMENT '修改时间'")
    private Date updateTime;

    @Column(name = "update_user", columnDefinition = "bigint(20) COMMENT '修改用户'")
    private Long updateUser;

    @OneToOne(cascade = CascadeType.PERSIST)
    @PrimaryKeyJoinColumn
    @JoinColumn(name = "default_channels_id", columnDefinition = "bigint(20) COMMENT '默认频道ID'")
    private ChannelsInfo channels;


    /**
     * 普通用户
     */
    @JsonIgnore
    @OneToMany
    @JoinTable(name = "user_chanenls", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "channels_id", referencedColumnName = "id")})
    private Set<ChannelsInfo> channelsSet;

    /**
     * 管理员用户
     */
    @JsonIgnore
    @OneToMany
    @JoinTable(name = "chanenls_admin", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "channels_id", referencedColumnName = "id")})
    private Set<ChannelsInfo> chanenlsAdmin;

    public @interface Update {
    }
}