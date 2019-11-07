package com.general.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.Set;

/**
 * @author L
 * @date 2019-03-25
 */
@Entity
@Data
@Table(name = "dept")
public class Dept implements Serializable {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 名称
     */
    @Column(name = "name", nullable = false, length = 50)
    @NotBlank
    private String name;

    /**
     * 启用/禁用
     */
    @NotNull
    private Boolean enabled;

    /**
     * 上级组织
     */
    @Column(name = "pid", nullable = false)
    @NotNull
    private Long pid;

    /**
     * 组织内最大人数
     */
    @Column(name = "max_person_number")
    private Integer maxPersonNumber;

    /**
     * 企业识别码
     */
    @Column(name = "enterprise_code", length = 50)
    private String enterpriseCode;

    @JsonIgnore
    @ManyToMany(mappedBy = "depts")
    private Set<Role> roles;

    @Column(name = "create_time")
    @CreationTimestamp
    private Timestamp createTime;

    public @interface Update {
    }


}