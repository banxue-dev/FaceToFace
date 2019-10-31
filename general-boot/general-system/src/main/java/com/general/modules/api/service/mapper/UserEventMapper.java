package com.general.modules.api.service.mapper;

import com.general.mapper.EntityMapper;
import com.general.modules.api.domain.UserEvent;
import com.general.modules.api.service.dto.UserEventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author L
* @date 2019-10-28
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEventMapper extends EntityMapper<UserEventDTO, UserEvent> {

}