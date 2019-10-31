package com.general.modules.api.service.mapper;

import com.general.mapper.EntityMapper;
import com.general.modules.api.domain.RecordFileInfo;
import com.general.modules.api.service.dto.RecordFileInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author L
* @date 2019-10-28
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecordFileInfoMapper extends EntityMapper<RecordFileInfoDTO, RecordFileInfo> {

}