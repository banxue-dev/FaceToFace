package com.general.modules.system.service.mapper;

import com.general.mapper.EntityMapper;
import com.general.modules.system.domain.ChannelsInfo;
import com.general.modules.system.service.dto.ChannelsInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author L
* @date 2019-10-24
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChannelsInfoMapper extends EntityMapper<ChannelsInfoDTO, ChannelsInfo> {

}