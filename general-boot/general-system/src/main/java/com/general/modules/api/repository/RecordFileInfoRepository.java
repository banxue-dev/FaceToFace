package com.general.modules.api.repository;

import com.general.modules.api.domain.RecordFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author L
* @date 2019-10-28
*/
public interface RecordFileInfoRepository extends JpaRepository<RecordFileInfo, Long>, JpaSpecificationExecutor {
}