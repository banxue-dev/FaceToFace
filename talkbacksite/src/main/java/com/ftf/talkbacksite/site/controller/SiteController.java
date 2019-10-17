package com.ftf.talkbacksite.site.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ftf.talkbacksite.site.dao.TimeSiteDao;
import com.ftf.talkbacksite.site.entity.TimeSite;
import com.ftf.talkbacksite.site.service.ISiteService;
import com.ftf.talkbacksite.utils.SnowflakeIdWorker;
import com.ftf.talkbacksite.utils.StringUtils;
import com.ftf.talkbacksite.utils.TimeUtils;

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

	@PostMapping("/hello")
	@ResponseBody
	public String hello() {

		return "hello word";
	}

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
//		mongoTemplate.insert(p1, TimeUtils.getCurrentTime("yyyy-MM-dd"));
		TimeSite n =mongoTemplate.findById(id, TimeSite.class,TimeUtils.getCurrentTime("yyyy-MM-dd"));
//		timeSiteDao.insert(p1);
		return n.getSiteJson();
	}
	@PostMapping("/getByUserIdAndTimes")
	@ResponseBody
	public String getByUserIdAndTimes(String userId,String startTime,String endTime) {
		TimeSite p1 = new TimeSite();
		p1.setCreateTime(TimeUtils.getCurrentTime());
		p1.setSiteJson("{'x':'1'}");
		p1.setUserId("1");
		p1.setSid(snowflakeIdWorker.nextId() + "");
//		TimeSite n =mongoTemplate.findById(id, TimeSite.class,TimeUtils.getCurrentTime("yyyy-MM-dd"));
		Query query=new Query();
		Criteria criter=new Criteria();
		criter.and("userId").is(userId);
		if(StringUtils.isNotNull(startTime)) {
			criter.and("createTime").gte(startTime);
		}
		if(StringUtils.isNotNull(endTime)) {
			criter.and("createTime").lte(endTime);
		}
		query.addCriteria(criter);
		List<TimeSite> lst=mongoTemplate.find(query, TimeSite.class,TimeUtils.getCurrentTime("yyyy-MM-dd"));
		for(TimeSite ts:lst) {
			System.out.println(ts.getSiteJson());
		}
		return "成功";
	}
}
