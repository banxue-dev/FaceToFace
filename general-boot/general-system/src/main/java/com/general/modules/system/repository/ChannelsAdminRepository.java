package com.general.modules.system.repository;

import com.general.modules.system.domain.ChanenlsAdmin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author L
* @date 2019-10-24
*/
public interface ChannelsAdminRepository extends JpaRepository<ChanenlsAdmin, Long>, JpaSpecificationExecutor {

	List<ChanenlsAdmin> findByChannelsId(Long channelsId);
	List<ChanenlsAdmin> findByUserId(Long userId);
	Integer deleteByUserId(Long userId);
	Integer deleteByChannelsId(Long channelsId);

}