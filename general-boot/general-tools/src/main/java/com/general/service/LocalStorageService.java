package com.general.service;

import com.general.domain.LocalStorage;
import com.general.service.dto.LocalStorageDTO;
import com.general.service.dto.LocalStorageQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
* @author L
* @date 2019-09-05
*/
@CacheConfig(cacheNames = "localStorage")
public interface LocalStorageService {

    /**
    * queryAll 分页
    * @param criteria
    * @param pageable
    * @return
    */
    @Cacheable
    Object queryAll(LocalStorageQueryCriteria criteria, Pageable pageable);

    /**
    * queryAll 不分页
    * @param criteria
    * @return
    */
    @Cacheable
    public Object queryAll(LocalStorageQueryCriteria criteria);

    /**
     * findById
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    LocalStorageDTO findById(Long id);

    /**
     * create
     * @param name
     * @param file
     * @return
     */
    @CacheEvict(allEntries = true)
    LocalStorageDTO create(String name, MultipartFile file);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(LocalStorage resources);

    /**
     * delete
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);

    @CacheEvict(allEntries = true)
    void deleteAll(Long[] ids);
}