package com.ftf.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ftf.entity.TimeSite;
import com.ftf.service.ISiteService;
import com.ftf.utils.FileLog;
import com.ftf.utils.R;
import com.ftf.utils.SnowflakeIdWorker;
import com.ftf.utils.StringUtils;
import com.ftf.utils.TimeUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexModel;

/**
 * 作者：fengchase 时间：2019年10月17日 文件：SiteServiceImpl.java 项目：talkbacksite
 */
@Service
public class SiteServiceImpl implements ISiteService {

	@Autowired
	private SnowflakeIdWorker snowflakeIdWorker;
	@Autowired
	private MongoTemplate mongoTemplate;

	/*
	 * 只能获取最近几天的数据
	 * eg：3  表示只能获取今天、昨天、前天的数据
	 */
	private  int lastDay=2;
	@Override
	public R add(TimeSite p1) {
		try {
			p1.setCreateTime(TimeUtils.getCurrentTime());
			p1.setSid(snowflakeIdWorker.nextId() + "");
			String collectionName = TimeUtils.getCurrentTime("yyyy-MM-dd");
			if(mongoTemplate.collectionExists(collectionName)) {
				mongoTemplate.insert(p1,collectionName );
			}else {
				mongoTemplate.insert(p1,collectionName );
				MongoCollection<Document> mongoCollection=mongoTemplate.getDb().getCollection(collectionName);
				List<IndexModel> indexes = new ArrayList<>();
				IndexModel model1 = new IndexModel(BasicDBObject.parse("{'createTime':-1}"));
				IndexModel model2 = new IndexModel(BasicDBObject.parse("{'userId':-1}"));
				indexes.add(model1);
				indexes.add(model2);
				mongoCollection.createIndexes(indexes);
			}
			return R.ok();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return R.error();
	}

	@Override
	public R getById(String id) {
		TimeSite n = mongoTemplate.findById(id, TimeSite.class, TimeUtils.getCurrentTime("yyyy-MM-dd"));
		return R.okdata(n);
	}

	@Override
	public R getByUserIdAndTimes(String userId, String startTime, String endTime) {

		List<TimeSite> lst = new LinkedList<TimeSite>();
		if (StringUtils.isNull(userId)) {
			return R.error("用户id不能为空");
		}

		/*
		 * 后面会根据这个相差天数来获取数据，
		 * d=1 表示当天
		 */
		int d=1;
		try {
			/*
			 * 默认的要查询的mongoDB的集合名称
			 * 也作为当前时间
			 */
			String curentime = TimeUtils.getCurrentTime("yyyy-MM-dd");

			/*
			 * 构造条件
			 */
			Query query = new Query();
			Criteria criter = new Criteria();
			criter.and("userId").is(userId);
			if (StringUtils.isNotNulls(startTime, endTime)) {
				/*
				 * 都不为空
				 */
				if (TimeUtils.compareDate(endTime, startTime,"yyyy-MM-dd") == 1) {
					/*
					 * 只能查询近3天的数据
					 */
					d=TimeUtils.getDayCompareDate(curentime,startTime , "yyyy-MM-dd");
					if(d>lastDay || d<0) {
						/*
						 * 超过了3天则不查询
						 */
						return R.error("只能查询最近3天的数据");
					}else {
						/*
						 * 这里17号和15号之前的差值这里是2，因为要算上当天，而且下面的for循环是从0开始的，所以这里加个1
						 */
						d+=1;
					}
					
				} else {
					/*
					 * 开始时间大于了结束时间
					 */
					return R.error("开始时间必须小于结束时间");
				}
				criter.andOperator(Criteria.where("createTime").gte(startTime),Criteria.where("createTime").lte(endTime));
			}else {
				/**
				 * 有一个为空，就只能有一个
				 */
				if (StringUtils.isNotNull(startTime)) {
					criter.and("createTime").gte(startTime);
				}else if (StringUtils.isNotNull(endTime)) {
					criter.and("createTime").lte(endTime);
				}
			}
			query.addCriteria(criter);
			for(int i=0;i<d;i++) {
				String collectionName=TimeUtils.getTimeDayAfterV2("yyyy-MM-dd", curentime, i);
				if(mongoTemplate.collectionExists(collectionName)) {
					List<TimeSite> clst= mongoTemplate.find(query, TimeSite.class, collectionName);
					if(clst!=null && clst.size()>0) {
						lst.addAll(clst);
					}
				}
			}
			FileLog.debugLog("test日志");
			return R.okdata(lst);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("服务器出现异常");
		}
	}
	public static void main(String[] args) {
		int d=0;
		try {
			//d = TimeUtils.getDayCompareDate("2019-10-17","2019-10-15" , "yyyy-MM-dd");
			String collectionName=TimeUtils.getTimeDayAfterV2("yyyy-MM-dd", new Date(), 0);
			System.out.println(collectionName);
		} catch (Exception e) {
			// TODO 打印输出日志
			e.printStackTrace();
		}
		System.out.println(d);//
	}
}
