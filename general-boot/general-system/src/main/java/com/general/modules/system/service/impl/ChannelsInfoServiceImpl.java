package com.general.modules.system.service.impl;

import java.util.*;

import com.general.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.general.exception.EntityExistException;
import com.general.modules.system.domain.ChanenlsAdmin;
import com.general.modules.system.domain.ChanenlsUser;
import com.general.modules.system.domain.ChannelsInfo;
import com.general.modules.system.domain.User;
import com.general.modules.system.repository.ChannelsAdminRepository;
import com.general.modules.system.repository.ChannelsInfoRepository;
import com.general.modules.system.repository.ChannelsUserRepository;
import com.general.modules.system.repository.UserRepository;
import com.general.modules.system.service.ChannelsInfoService;
import com.general.modules.system.service.dto.ChannelsInfoDTO;
import com.general.modules.system.service.dto.ChannelsInfoQueryCriteria;
import com.general.modules.system.service.mapper.ChannelsInfoMapper;
import com.general.utils.QueryHelp;
import com.general.utils.ValidationUtil;

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
	@Autowired
	private ChannelsUserRepository channelsUserRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ChannelsAdminRepository channelsAdminRepository;

	@Override
	public Map<String, Object> queryAll(ChannelsInfoQueryCriteria criteria, Pageable pageable) {
		Page<ChannelsInfo> page = channelsInfoRepository.findAll(
				(root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder),
				pageable);
		List<ChannelsInfoDTO> lst = channelsInfoMapper.toDto(page.getContent());
		for (ChannelsInfoDTO t : lst) {
			List<ChanenlsUser> lstcus = channelsUserRepository.findByChannelsId(t.getId());
			List<ChanenlsAdmin> lstcas = channelsAdminRepository.findByChannelsId(t.getId());
			if (lstcus != null && lstcus.size() > 0) {
				Set<User> use=new HashSet<User>();
				for (ChanenlsUser cu : lstcus) {
					User u=userRepository.findById(cu.getUserId()).get();
					u.setRoles(null);
					if(u!=null) {
						use.add(u);
					}
				}
				t.setUserSet(use);
			}
			if (lstcas != null && lstcas.size() > 0) {
				Set<User> use=new HashSet<User>();
				for (ChanenlsAdmin cu : lstcas) {
					User u=userRepository.findById(cu.getUserId()).get();
					u.setRoles(null);
					if(u!=null) {
						use.add(u);
					}
				}
				t.setUserAdmin(use);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalElements", lst.size());
		map.put("content", lst);
		return map;
	}

	@Override
	public List<ChannelsInfoDTO> queryAll(ChannelsInfoQueryCriteria criteria) {
		return channelsInfoMapper.toDto(channelsInfoRepository.findAll(
				(root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
	}

	@Override
	public List<ChannelsInfo> queryAllRe(ChannelsInfoQueryCriteria criteria) {
		return channelsInfoRepository.findAll(
				(root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
	}

	@Override
	public ChannelsInfoDTO findById(Long id) {
		Optional<ChannelsInfo> channelsInfo = channelsInfoRepository.findById(id);
		ValidationUtil.isNull(channelsInfo, "ChannelsInfo", "id", id);
		return channelsInfoMapper.toDto(channelsInfo.get());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ChannelsInfoDTO create(ChannelsInfo resources) {
		// 验证频道是否存在
		if (channelsInfoRepository.findByChannelsName(resources.getChannelsName()) != null) {
			throw new EntityExistException(ChannelsInfo.class, "channelsName", resources.getChannelsName());
		}
		ChannelsInfoDTO res = channelsInfoMapper.toDto(channelsInfoRepository.save(resources));
		if (resources.getUserSet() != null && resources.getUserSet().size() > 0) {
			List<ChanenlsUser> lst = new ArrayList<ChanenlsUser>();
			for (User t : resources.getUserSet()) {
				ChanenlsUser s = new ChanenlsUser();
				s.setUserId(t.getId());
				s.setChannelsId(res.getId());
				lst.add(s);
			}
			channelsUserRepository.saveAll(lst);
		}
		if (resources.getUserAdmin() != null && resources.getUserAdmin().size() > 0) {
			List<ChanenlsAdmin> lst = new ArrayList<ChanenlsAdmin>();
			for (User t : resources.getUserAdmin()) {
				ChanenlsAdmin s = new ChanenlsAdmin();
				s.setUserId(t.getId());
				s.setChannelsId(res.getId());
				lst.add(s);
			}
			channelsAdminRepository.saveAll(lst);
		}
		// 验证频道内最大人数

		return res;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(ChannelsInfo resources) {
		Optional<ChannelsInfo> optionalChannelsInfo = channelsInfoRepository.findById(resources.getId());
		ValidationUtil.isNull(optionalChannelsInfo, "ChannelsInfo", "id", resources.getId());
		ChannelsInfo channelsInfo = optionalChannelsInfo.get();
		channelsInfo.copy(resources);
		channelsInfoRepository.save(channelsInfo);
		if (resources.getUserSet() != null && resources.getUserSet().size() > 0) {
			channelsUserRepository.deleteByChannelsId(resources.getId());
			List<ChanenlsUser> lst = new ArrayList<ChanenlsUser>();
			for (User t : resources.getUserSet()) {
				ChanenlsUser s = new ChanenlsUser();
				s.setUserId(t.getId());
				s.setChannelsId(resources.getId());
				lst.add(s);
			}
			channelsUserRepository.saveAll(lst);
		}
		if (resources.getUserAdmin() != null && resources.getUserAdmin().size() > 0) {
			channelsAdminRepository.deleteByChannelsId(resources.getId());
			List<ChanenlsAdmin> lst = new ArrayList<ChanenlsAdmin>();
			for (User t : resources.getUserAdmin()) {
				ChanenlsAdmin s = new ChanenlsAdmin();
				s.setUserId(t.getId());
				s.setChannelsId(resources.getId());
				lst.add(s);
			}
			channelsAdminRepository.saveAll(lst);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		channelsInfoRepository.deleteById(id);
	}

	@Override
	public Map<String, Object> getChannelsChartData(Set<Long> deptIds) {
		Map<String, Object> map = new HashMap<>();
		map.put("total", channelsInfoRepository.getChannelsTotal(deptIds));
		//获取日期
		List<String> timeList = TimeUtils.listHistory7DaysDate(new Date());
		//数量
		List<Integer> countList = new ArrayList<>();
		for (String time : timeList) {
			Integer count = channelsInfoRepository.getChannelsCountByCreateTime(time, deptIds);
			countList.add(count);
		}
		map.put("days", timeList);
		map.put("counts", countList);
		return map;
	}

	@Override
	public ChannelsInfo findByIdRe(Long id) {
		// TODO Auto-generated method stub
		Optional<ChannelsInfo> channelsInfo = channelsInfoRepository.findById(id);
		ValidationUtil.isNull(channelsInfo, "ChannelsInfo", "id", id);
		return channelsInfo.get();
	}
}