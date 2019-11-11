package com.general.modules.system.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author L
 * @date 2019-10-24
 */
@Entity
@Data
@Table(name = "channels_info")
public class ChannelsInfo implements Serializable {
    /**
     * 频道ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = ChannelsInfo.Update.class)
    private Long id;

    /**
     * 频道名(当前组织内唯一，不可修改)
     */
    @Column(name = "channels_name", columnDefinition = "varchar(50) COMMENT '频道名'")
    private String channelsName;

    /**
     * 所属组织
     */
    @OneToOne
    @JoinColumn(name = "dept_id", columnDefinition = "bigint(20) COMMENT '组织ID'")
    private Dept dept;

    /**
     * 频道属性（音频，音视频）
     */
    @Column(name = "attr", columnDefinition = "int(2) COMMENT '频道属性（0音频，1音视频）'")
    private Integer attr;

    /**
     * 频道模式（申请发言，自由发言，静默）
     */
    @Column(name = "mode", columnDefinition = "int(2) COMMENT '频道模式（0申请发言，1自由发言，2静默）'")
    private Integer mode;

    /**
     * 频道内最大人数
     */
    @Column(name = "max_person_number", columnDefinition = "int(10) COMMENT '频道内最大人数'")
    private Integer maxPersonNumber;

    /**
     * 频道录音开关
     */
    @Column(name = "record_switch", columnDefinition = "int(2) COMMENT '频道录音开关(1开，0关)'")
    private Boolean recordSwitch;

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

    /**
     * 管理员用户
     */
    @OneToMany
    @JoinTable(name = "user_chanenls", joinColumns = {@JoinColumn(name = "channels_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<User> userSet;

    /**
     * 管理员用户
     */
    @OneToMany
    @JoinTable(name = "chanenls_admin", joinColumns = {@JoinColumn(name = "channels_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<User> userAdmin;

    public @interface Update {
    }

    public void copy(ChannelsInfo source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}