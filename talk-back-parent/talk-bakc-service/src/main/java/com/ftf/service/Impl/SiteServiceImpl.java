package com.ftf.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ftf.entity.TimeSite;
import com.ftf.service.ISiteService;
import com.ftf.utils.SnowflakeIdWorker;
import com.ftf.utils.StringUtils;
import com.ftf.utils.TimeUtils;

/**
作者：fengchase
时间：2019年10月17日
文件：SiteServiceImpl.java
项目：talkbacksite
*/
@Service
public class SiteServiceImpl implements ISiteService {

	@Autowired
	private SnowflakeIdWorker snowflakeIdWorker;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Override
	public Integer add(TimeSite p1) {
		try {
			p1.setCreateTime(TimeUtils.getCurrentTime());
			p1.setSid(snowflakeIdWorker.nextId() + "");
			mongoTemplate.insert(p1, TimeUtils.getCurrentTime("yyyy-MM-dd"));
			return 1;
		}catch(Exception e) {
			
		}
		return 0;
	}
	@Override
	public TimeSite getById(String id) {
		TimeSite n =mongoTemplate.findById(id, TimeSite.class,TimeUtils.getCurrentTime("yyyy-MM-dd"));
		return n;
	}
	@Override
	public List<TimeSite> getByUserIdAndTimes(String userId,String startTime,String endTime) {

		List<TimeSite> lst=new ArrayList<TimeSite>();
		if(StringUtils.isNull(userId)) {
			return lst;
		}
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
		lst=mongoTemplate.find(query, TimeSite.class,TimeUtils.getCurrentTime("yyyy-MM-dd"));
		return lst;
	}
}

