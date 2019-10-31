package com.general.modules.api.repository;

import com.general.modules.api.domain.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author L
* @date 2019-10-28
*/
public interface UserEventRepository extends JpaRepository<UserEvent, Long>, JpaSpecificationExecutor {
}