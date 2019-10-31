package com.general.modules.api.service.impl;

import com.general.modules.api.domain.UserEvent;
import com.general.modules.api.repository.UserEventRepository;
import com.general.modules.api.service.UserEventService;
import com.general.modules.api.service.dto.UserEventDTO;
import com.general.modules.api.service.dto.UserEventQueryCriteria;
import com.general.modules.api.service.mapper.UserEventMapper;
import com.general.utils.PageUtil;
import com.general.utils.QueryHelp;
import com.general.utils.ValidationUtil;
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
* @date 2019-10-28
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserEventServiceImpl implements UserEventService {

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private UserEventMapper userEventMapper;

    @Override
    public Map<String,Object> queryAll(UserEventQueryCriteria criteria, Pageable pageable){
        Page<UserEvent> page = userEventRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(userEventMapper::toDto));
    }

    @Override
    public List<UserEventDTO> queryAll(UserEventQueryCriteria criteria){
        return userEventMapper.toDto(userEventRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public UserEventDTO findById(Long id) {
        Optional<UserEvent> userEvent = userEventRepository.findById(id);
        ValidationUtil.isNull(userEvent,"UserEvent","id",id);
        return userEventMapper.toDto(userEvent.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserEventDTO create(UserEvent resources) {
        return userEventMapper.toDto(userEventRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserEvent resources) {
        Optional<UserEvent> optionalUserEvent = userEventRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalUserEvent,"UserEvent","id",resources.getId());
        UserEvent userEvent = optionalUserEvent.get();
        userEvent.copy(resources);
        userEventRepository.save(userEvent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        userEventRepository.deleteById(id);
    }
}