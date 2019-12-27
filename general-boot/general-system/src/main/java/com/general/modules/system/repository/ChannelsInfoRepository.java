package com.general.modules.system.repository;

import com.general.modules.system.domain.ChannelsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * @author L
 * @date 2019-10-24
 */
public interface ChannelsInfoRepository extends JpaRepository<ChannelsInfo, Long>, JpaSpecificationExecutor {

    /**
     * @Description: 根据频道名查询
     * @Author LuoJing
     * @Date 2019/11/11 13:33
     */
    ChannelsInfo findByChannelsName(String channelsName);



    @Query(value = "SELECT count(1) FROM channels_info where dept_id in (:deptIds)", nativeQuery = true)
    Integer getChannelsTotal(@Param(value = "deptIds") Set<Long> deptIds);

    @Query(value = "SELECT count(1) FROM channels_info where DATE_FORMAT(create_time,'%Y-%m-%d') = :time and dept_id in (:deptIds)", nativeQuery = true)
    Integer getChannelsCountByCreateTime(@Param(value = "time") String time, @Param(value = "deptIds") Set<Long> deptIds);

    @Query(value = "SELECT count(1) FROM channels_info where DATE_FORMAT(create_time,'%Y-%m-%d') <= :time and dept_id in (:deptIds)", nativeQuery = true)
    Integer getChannelsTotalByCreateTime(@Param(value = "time") String time, @Param(value = "deptIds") Set<Long> deptIds);

}