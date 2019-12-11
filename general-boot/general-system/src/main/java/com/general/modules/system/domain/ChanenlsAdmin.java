package com.general.modules.system.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 频道管理员表
 * @author fengchaseyou
 *
 */
@Entity
@Data
@Table(name = "chanenls_admin")
public class ChanenlsAdmin implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "channels_id", columnDefinition = "bigint(20) COMMENT '频道id'")
	private Long channelsId;
	@Column(name = "user_id", columnDefinition = "bigint(20) COMMENT '用户id'")
	private Long userId;
}
