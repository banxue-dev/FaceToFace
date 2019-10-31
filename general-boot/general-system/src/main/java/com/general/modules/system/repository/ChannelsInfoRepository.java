package com.general.modules.system.repository;

import com.general.modules.system.domain.ChannelsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author L
* @date 2019-10-24
*/
public interface ChannelsInfoRepository extends JpaRepository<ChannelsInfo, Long>, JpaSpecificationExecutor {
}