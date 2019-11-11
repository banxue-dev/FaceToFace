package com.general.modules.system.service.impl;

import com.general.exception.EntityExistException;
import com.general.modules.system.domain.ChannelsInfo;
import com.general.modules.system.service.ChannelsInfoService;
import com.general.modules.system.service.mapper.ChannelsInfoMapper;
import com.general.utils.PageUtil;
import com.general.utils.QueryHelp;
import com.general.utils.ValidationUtil;
import com.general.modules.system.repository.ChannelsInfoRepository;
import com.general.modules.system.service.dto.ChannelsInfoDTO;
import com.general.modules.system.service.dto.ChannelsInfoQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

/**
* @author L
* @date 2019-10-24
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChannelsInfoServiceImpl implements ChannelsInfoService {

    @Autowired
    private ChannelsInfoRepository channelsInfoRepository;

    @Autowired
    private ChannelsInfoMapper channelsInfoMapper;

    @Override
    public Map<String,Object> queryAll(ChannelsInfoQueryCriteria criteria, Pageable pageable){
        Page<ChannelsInfo> page = channelsInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(channelsInfoMapper::toDto));
    }

    @Override
    public List<ChannelsInfoDTO> queryAll(ChannelsInfoQueryCriteria criteria){
        return channelsInfoMapper.toDto(channelsInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public ChannelsInfoDTO findById(Long id) {
        Optional<ChannelsInfo> channelsInfo = channelsInfoRepository.findById(id);
        ValidationUtil.isNull(channelsInfo,"ChannelsInfo","id",id);
        return channelsInfoMapper.toDto(channelsInfo.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChannelsInfoDTO create(ChannelsInfo resources) {
        //验证频道是否存在
        if(channelsInfoRepository.findByChannelsName(resources.getChannelsName()) != null){
            throw new EntityExistException(ChannelsInfo.class,"channelsName",resources.getChannelsName());
        }
        //验证频道内最大人数

        return channelsInfoMapper.toDto(channelsInfoRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ChannelsInfo resources) {
        Optional<ChannelsInfo> optionalChannelsInfo = channelsInfoRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalChannelsInfo,"ChannelsInfo","id",resources.getId());
        ChannelsInfo channelsInfo = optionalChannelsInfo.get();
        channelsInfo.copy(resources);
        channelsInfoRepository.save(channelsInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        channelsInfoRepository.deleteById(id);
    }
}