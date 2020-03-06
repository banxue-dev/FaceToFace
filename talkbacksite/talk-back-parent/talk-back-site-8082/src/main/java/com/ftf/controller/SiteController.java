package com.ftf.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ftf.entity.TimeSite;
import com.ftf.service.ISiteService;
import com.ftf.utils.R;

/**
 * 作者：fengchase 时间：2019年10月16日 文件：SiteController.java 项目：talkbacksite
 */
@Controller()
@RequestMapping("/site")
public class SiteController {

	@Autowired
	private ISiteService siteService;

	@PostMapping("/add")
	@ResponseBody
	public R add(String siteJson,String userId,Integer upDownState,String locationtime) {
		TimeSite p1 = new TimeSite();
		p1.setSiteJson(siteJson);
		p1.setUserId(userId);
		p1.setLocationTime(locationtime);
		p1.setUpDownState(upDownState);
		R r=siteService.add(p1);
		return r;
	}
	@PostMapping("/getById")
	@ResponseBody
	public R getById(String id,String collectionName) {
		R r=siteService.getById(id,collectionName);
		return r;
	}
	@PostMapping("/getByUserIdAndTimes")
	@ResponseBody
	public R getByUserIdAndTimes(String userId,String startTime,String endTime,String pageNum,String pageSize,int timeType) {
		
		R r=siteService.getByUserIdAndTimes( userId, startTime, endTime,pageNum,pageSize, timeType);
		return r;
	}
}
