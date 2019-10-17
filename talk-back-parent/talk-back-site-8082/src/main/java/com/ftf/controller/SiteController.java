package com.ftf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ftf.dao.TimeSiteDao;
import com.ftf.entity.TimeSite;
import com.ftf.service.ISiteService;

/**
 * 作者：fengchase 时间：2019年10月16日 文件：SiteController.java 项目：talkbacksite
 */
@Controller()
@RequestMapping("/site")
public class SiteController {

	@Autowired
	private TimeSiteDao timeSiteDao;
	
	@Autowired
	private ISiteService siteService;

	@PostMapping("/add")
	@ResponseBody
	public String add() {
		TimeSite p1 = new TimeSite();
		p1.setSiteJson("{'x':'1'}");
		p1.setUserId("1");
		siteService.add(p1);
		return "成功";
	}
	@PostMapping("/getById")
	@ResponseBody
	public String getById(String id) {
		TimeSite n =siteService.getById(id);
		return n.getSiteJson();
	}
	@PostMapping("/getByUserIdAndTimes")
	@ResponseBody
	public String getByUserIdAndTimes(String userId,String startTime,String endTime) {
		
		List<TimeSite> lst=siteService.getByUserIdAndTimes( userId, startTime, endTime);
		for(TimeSite ts:lst) {
			System.out.println(ts.getSiteJson());
		}
		return "成功,8082";
	}
}
