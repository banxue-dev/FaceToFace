package com.general.modules.api.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description: 账号事件
 * @Author LuoJing
 * @Date 2019/10/2817:36
 */
@Entity
@Data
@Table(name = "user_event")
public class UserEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = UserEvent.Update.class)
    private Long id;

    @Column(name = "user_id", columnDefinition = "bigint(20) COMMENT '账号ID（用户ID）'")
    private Long userId;

    @Column(name = "event", columnDefinition = "int(1) COMMENT '事件：0上线，1下线'")
    private Integer event;

    @CreationTimestamp
    @Column(name = "time", columnDefinition = "datetime COMMENT '时间'")
    private Date time;

    public @interface Update {
    }

    public void copy(UserEvent source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
