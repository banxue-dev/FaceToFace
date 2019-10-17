package com.ftf.talkbacksite.site.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ftf.talkbacksite.site.entity.TimeSite;

/**
作者：fengchase
时间：2019年10月16日
文件：MongoDBDao.java
项目：talkbacksite
*/
public interface TimeSiteDao extends MongoRepository<TimeSite,String> {
    TimeSite findByUserIdEquals(String id);
}

