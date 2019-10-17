package com.ftf.service;

import java.util.List;

import com.ftf.entity.TimeSite;

/**
作者：fengchase
时间：2019年10月17日
文件：ISiteService.java
项目：talkbacksite
*/
public interface ISiteService {

	/**
	 * 根据id获取数据
	 * @param id
	 * @return
	 * 2019年10月17日
	 * 作者：fengchase
	 */
	TimeSite getById(String id);

	/**
	 * 根据用户id，时间段查询，
	 * @param userId 不可为空
	 * @param startTime  可以为空
	 * @param endTime  可以为空
	 * @return
	 * 2019年10月17日
	 * 作者：fengchase
	 */
	List<TimeSite> getByUserIdAndTimes(String userId, String startTime, String endTime);

	/**
	 * 添加一条数据
	 * sid字段和添加时间字段无需设置
	 * @param p1
	 * @return
	 * 2019年10月17日
	 * 作者：fengchase
	 */
	Integer add(TimeSite p1);

}

