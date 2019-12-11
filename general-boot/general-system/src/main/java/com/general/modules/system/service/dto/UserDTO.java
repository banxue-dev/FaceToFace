package com.general.modules.system.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.general.modules.system.domain.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * @author L
 * @date 2018-11-23
 */
public class UserDTO implements Serializable {

    @ApiModelProperty(hidden = true)
    private Long id;

    private String username;

    private String avatar;

    private String email;

    private String phone;

    private Boolean enabled;
    
    /**
     * 用户类型0:普通用户,1:管理用户
     */
    private Integer  userType;

    @JsonIgnore
    private String password;

    private Timestamp createTime;

    private Date lastPasswordResetTime;

    @ApiModelProperty(hidden = true)
    private Set<RoleSmallDTO> roles;

    private DeptSmallDTO dept;

    private Long deptId;
    
    private Long defaultChannelsId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 默认频道
     */
    private ChannelsInfo channels;

    /**
     * 频道信息
     */
    private Set<ChannelsInfo> channelsSet;

    /**
     * 企业识别码
     */
    private String enterpriseCode;

    /**
     * 定位开关
     */
    private Boolean locationSwitch;

    /**
     * 定位间隔
     */
    private String locationInterval;

    /**
     * 视频功能开关
     */
    private Boolean videoSwitch;

    /**
     * 服务期限
     */
    private String serviceTime;

    private Long createUser;

    private Date updateTime;

    private Long updateUser;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Date getLastPasswordResetTime() {
		return lastPasswordResetTime;
	}

	public void setLastPasswordResetTime(Date lastPasswordResetTime) {
		this.lastPasswordResetTime = lastPasswordResetTime;
	}

	public Set<RoleSmallDTO> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleSmallDTO> roles) {
		this.roles = roles;
	}

	public DeptSmallDTO getDept() {
		return dept;
	}

	public void setDept(DeptSmallDTO dept) {
		this.dept = dept;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public ChannelsInfo getChannels() {
		return channels;
	}

	public void setChannels(ChannelsInfo channels) {
		this.channels = channels;
	}

	public Set<ChannelsInfo> getChannelsSet() {
		return channelsSet;
	}

	public void setChannelsSet(Set<ChannelsInfo> channelsSet) {
		this.channelsSet = channelsSet;
	}

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public Boolean getLocationSwitch() {
		return locationSwitch;
	}

	public void setLocationSwitch(Boolean locationSwitch) {
		this.locationSwitch = locationSwitch;
	}

	public String getLocationInterval() {
		return locationInterval;
	}

	public void setLocationInterval(String locationInterval) {
		this.locationInterval = locationInterval;
	}

	public Boolean getVideoSwitch() {
		return videoSwitch;
	}

	public void setVideoSwitch(Boolean videoSwitch) {
		this.videoSwitch = videoSwitch;
	}

	public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}

	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}

	public Long getDefaultChannelsId() {
		return defaultChannelsId;
	}

	public void setDefaultChannelsId(Long defaultChannelsId) {
		this.defaultChannelsId = defaultChannelsId;
	}
    
	
    
}
