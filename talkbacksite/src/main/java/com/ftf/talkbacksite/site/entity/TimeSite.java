package com.ftf.talkbacksite.site.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
作者：fengchase
时间：2019年10月16日
文件：TimeSite.java
项目：talkbacksite
*/
@Document
public class TimeSite {


	@Id
	private String sid;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 位置json
	 */
	private String siteJson;
	
	private String userId;
	
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getSiteJson() {
		return siteJson;
	}
	public void setSiteJson(String siteJson) {
		this.siteJson = siteJson;
	}
	
	@Override
	public String toString() {
		// TODO 此处为方法主题
		return super.toString();
	}
	
	
}

