package com.general.modules.api.service;

import com.general.modules.api.domain.UserEvent;
import com.general.modules.api.service.dto.UserEventDTO;
import com.general.modules.api.service.dto.UserEventQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;

/**
* @author L
* @date 2019-10-28
*/
//@CacheConfig(cacheNames = "userEvent")
public interface UserEventService {

    /**
    * 查询数据分页
    * @param criteria
    * @param pageable
    * @return
    */
    //@Cacheable
    Map<String,Object> queryAll(UserEventQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria
    * @return
    */
    //@Cacheable
    List<UserEventDTO> queryAll(UserEventQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    //@Cacheable(key = "#p0")
    UserEventDTO findById(Long id);

    /**
     * 创建
     * @param resources
     * @return
     */
    //@CacheEvict(allEntries = true)
    UserEventDTO create(UserEvent resources);

    /**
     * 编辑
     * @param resources
     */
    //@CacheEvict(allEntries = true)
    void update(UserEvent resources);

    /**
     * 删除
     * @param id
     */
    //@CacheEvict(allEntries = true)
    void delete(Long id);
}