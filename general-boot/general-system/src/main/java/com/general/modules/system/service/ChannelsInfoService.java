package com.general.modules.system.service;

import com.general.modules.system.domain.ChannelsInfo;
import com.general.modules.system.service.dto.ChannelsInfoDTO;
import com.general.modules.system.service.dto.ChannelsInfoQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.List;
import java.util.Set;

/**
 * @author L
 * @date 2019-10-24
 */
@CacheConfig(cacheNames = "channelsInfo")
public interface ChannelsInfoService {

    /**
     * 查询数据分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
//    @Cacheable
    Map<String, Object> queryAll(ChannelsInfoQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria
     * @return
     */
    @Cacheable
    List<ChannelsInfoDTO> queryAll(ChannelsInfoQueryCriteria criteria);

    /**
     * 查询所有数据不分页
     *
     * @param criteria
     * @return
     */
    @Cacheable
    List<ChannelsInfo> queryAllRe(ChannelsInfoQueryCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    ChannelsInfoDTO findById(Long id);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    ChannelsInfo findByIdRe(Long id);

    /**
     * 创建
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    ChannelsInfoDTO create(ChannelsInfo resources);

    /**
     * 编辑
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(ChannelsInfo resources);

    /**
     * 删除
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);

    /**
     * @Description: 用户七天内的新增数量
     * @Author LuoJing
     * @Date 2019/12/17 14:38
     */
    Map<String, Object> getChannelsChartData(Set<Long> deptIds);
}