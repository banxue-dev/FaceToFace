package com.general.modules.api.service;

import com.general.modules.api.domain.RecordFileInfo;
import com.general.modules.api.service.dto.RecordFileInfoDTO;
import com.general.modules.api.service.dto.RecordFileInfoQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;

/**
* @author L
* @date 2019-10-28
*/
//@CacheConfig(cacheNames = "recordFileInfo")
public interface RecordFileInfoService {

    /**
    * 查询数据分页
    * @param criteria
    * @param pageable
    * @return
    */
    //@Cacheable
    Map<String,Object> queryAll(RecordFileInfoQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria
    * @return
    */
    //@Cacheable
    List<RecordFileInfoDTO> queryAll(RecordFileInfoQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    //@Cacheable(key = "#p0")
    RecordFileInfoDTO findById(Long id);

    /**
     * 创建
     * @param resources
     * @return
     */
    //@CacheEvict(allEntries = true)
    RecordFileInfoDTO create(RecordFileInfo resources);

    /**
     * 编辑
     * @param resources
     */
    //@CacheEvict(allEntries = true)
    void update(RecordFileInfo resources);

    /**
     * 删除
     * @param id
     */
    //@CacheEvict(allEntries = true)
    void delete(Long id);
}