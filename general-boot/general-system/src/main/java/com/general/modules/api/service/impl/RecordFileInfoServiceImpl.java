package com.general.modules.api.service.impl;

import com.general.modules.api.domain.RecordFileInfo;
import com.general.modules.api.service.mapper.RecordFileInfoMapper;
import com.general.modules.api.repository.RecordFileInfoRepository;
import com.general.modules.api.service.RecordFileInfoService;
import com.general.modules.api.service.dto.RecordFileInfoDTO;
import com.general.modules.api.service.dto.RecordFileInfoQueryCriteria;
import com.general.utils.PageUtil;
import com.general.utils.QueryHelp;
import com.general.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* @author L
* @date 2019-10-28
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RecordFileInfoServiceImpl implements RecordFileInfoService {

    @Autowired
    private RecordFileInfoRepository recordFileInfoRepository;

    @Autowired
    private RecordFileInfoMapper recordFileInfoMapper;

    @Override
    public Map<String,Object> queryAll(RecordFileInfoQueryCriteria criteria, Pageable pageable){
        Page<RecordFileInfo> page = recordFileInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(recordFileInfoMapper::toDto));
    }

    @Override
    public List<RecordFileInfoDTO> queryAll(RecordFileInfoQueryCriteria criteria){
        return recordFileInfoMapper.toDto(recordFileInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public RecordFileInfoDTO findById(Long id) {
        Optional<RecordFileInfo> recordFileInfo = recordFileInfoRepository.findById(id);
        ValidationUtil.isNull(recordFileInfo,"RecordFileInfo","id",id);
        return recordFileInfoMapper.toDto(recordFileInfo.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RecordFileInfoDTO create(RecordFileInfo resources) {
        return recordFileInfoMapper.toDto(recordFileInfoRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RecordFileInfo resources) {
        Optional<RecordFileInfo> optionalRecordFileInfo = recordFileInfoRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalRecordFileInfo,"RecordFileInfo","id",resources.getId());
        RecordFileInfo recordFileInfo = optionalRecordFileInfo.get();
        recordFileInfo.copy(resources);
        recordFileInfoRepository.save(recordFileInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        recordFileInfoRepository.deleteById(id);
    }
}