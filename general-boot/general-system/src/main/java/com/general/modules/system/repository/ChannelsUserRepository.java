package com.general.modules.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.general.modules.system.domain.ChanenlsUser;

/**
* @author L
* @date 2019-10-24
*/
public interface ChannelsUserRepository extends JpaRepository<ChanenlsUser, Long>, JpaSpecificationExecutor {

	List<ChanenlsUser> findByChannelsId(Long channelsId);
	List<ChanenlsUser> findByUserId(Long userId);
	Integer deleteByUserId(Long userId);
	Integer deleteByChannelsId(Long channelsId);

}