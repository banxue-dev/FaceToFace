package com.general.modules.api.domain.vo;

import com.general.modules.system.domain.ChannelsInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author LuoJing
 * @Date 2020/2/2011:48
 */
@Data
@ApiModel(description = "用户频道返回结果")
public class AppChannelsInfoVO {

    @ApiModelProperty(value = "默认频道")
    private ChannelsInfo defaultChannels;

    @ApiModelProperty(value = "频道集合")
    private List<ChannelsInfo> channels;

}
