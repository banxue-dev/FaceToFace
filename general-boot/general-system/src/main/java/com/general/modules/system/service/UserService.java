package com.general.modules.system.service;

import com.general.modules.system.domain.User;
import com.general.modules.system.service.dto.UserDTO;
import com.general.modules.system.service.dto.UserQueryCriteria;
import io.swagger.models.auth.In;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author L
 * @date 2018-11-23
 */
@CacheConfig(cacheNames = "user")
public interface UserService {

    /**
     * get
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    UserDTO findById(long id);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    UserDTO create(User resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(User resources);

    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(Long id);

    /**
     * findByName
     *
     * @param userName
     * @return
     */
    @Cacheable(key = "'loadUserByUsername:'+#p0")
    UserDTO findByName(String userName);

    /**
     * 修改密码
     *
     * @param username
     * @param encryptPassword
     */
    @CacheEvict(allEntries = true)
    void updatePass(String username, String encryptPassword);

    /**
     * @Description: 修改定位开关
     * @Author LuoJing
     * @Date 2019/11/1 16:14
     */
    @CacheEvict(allEntries = true)
    void updateLocationSwitch(Long id, Integer status);

    /**
     * @Description: 修改视频开关
     * @Author LuoJing
     * @Date 2019/11/1 16:14
     */
    @CacheEvict(allEntries = true)
    void updateVideoSwitch(Long id, Integer status);

    /**
     * 修改头像
     *
     * @param file
     */
    @CacheEvict(allEntries = true)
    void updateAvatar(MultipartFile file);

    /**
     * 修改邮箱
     *
     * @param username
     * @param email
     */
    @CacheEvict(allEntries = true)
    void updateEmail(String username, String email);

    @Cacheable
    Object queryAll(UserQueryCriteria criteria, Pageable pageable);

    @Cacheable
    List<UserDTO> queryAll(UserQueryCriteria criteria);

    void download(List<UserDTO> queryAll, HttpServletResponse response) throws IOException;
}
