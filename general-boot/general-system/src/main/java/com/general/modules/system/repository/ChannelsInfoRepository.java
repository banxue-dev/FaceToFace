package com.general.modules.system.repository;

import com.general.modules.system.domain.ChannelsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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

}