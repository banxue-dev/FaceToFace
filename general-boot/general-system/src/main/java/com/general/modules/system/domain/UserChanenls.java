package com.general.modules.system.domain;

import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @Description: 用户频道关系表
 * @Author LuoJing
 * @Date 2019/10/2914:11
 */
@Entity
@Data
@Table(name = "user_chanenls")
public class UserChanenls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = UserChanenls.Update.class)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", columnDefinition = "bigint(20) COMMENT '用户ID'")
    private User userId;

    @OneToOne
    @JoinColumn(name = "channels_id", columnDefinition = "bigint(20) COMMENT '频道ID'")
    private ChannelsInfo channelsId;

    @Column(name = "is_admin",columnDefinition = "int(1) COMMENT '是否是管理员：0是，1否'")
    private Integer isAdmin;

    @Column(name = "is_default",columnDefinition = "int(1) COMMENT '是否是默认频道：0是，1否'")
    private Integer isDefault;

    private @interface Update{
    }

}
