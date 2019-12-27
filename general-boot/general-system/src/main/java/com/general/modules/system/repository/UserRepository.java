package com.general.modules.system.repository;

import com.general.modules.system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Set;

/**
 * @author L
 * @date 2018-11-22
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor {

    /**
     * findByUsername
     *
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * findByEmail
     *
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 修改密码
     *
     * @param username
     * @param pass
     */
    @Modifying
    @Query(value = "update user set password = ?2 , last_password_reset_time = ?3 where username = ?1", nativeQuery = true)
    void updatePass(String username, String pass, Date lastPasswordResetTime);

    /**
     * @Description: 修改定位开关
     * @Author LuoJing
     * @Date 2019/11/1 16:36
     */
    @Modifying
    @Query(value = "update user set location_switch = ?2 where id = ?1", nativeQuery = true)
    void updateLocationSwitch(Long id, Integer status);

    /**
     * @Description: 修改视频开关
     * @Author LuoJing
     * @Date 2019/11/1 16:36
     */
    @Modifying
    @Query(value = "update user set video_switch = ?2 where id = ?1", nativeQuery = true)
    void updateVideoSwitch(Long id, Integer status);

    /**
     * 修改头像
     *
     * @param username
     * @param url
     */
    @Modifying
    @Query(value = "update user set avatar = ?2 where username = ?1", nativeQuery = true)
    void updateAvatar(String username, String url);

    /**
     * 修改邮箱
     *
     * @param username
     * @param email
     */
    @Modifying
    @Query(value = "update user set email = ?2 where username = ?1", nativeQuery = true)
    void updateEmail(String username, String email);

    @Query(value = "SELECT count(1) FROM user where dept_id in (:deptIds)", nativeQuery = true)
    Integer getUserTotal(@Param(value = "deptIds") Set<Long> deptIds);

    @Query(value = "SELECT count(1) FROM user where DATE_FORMAT(create_time,'%Y-%m-%d') = :time and  dept_id in (:deptIds)", nativeQuery = true)
    Integer getUserCountByCreateTime(@Param(value = "time") String time, @Param(value = "deptIds") Set<Long> deptIds);

    @Query(value = "SELECT count(1) FROM user where DATE_FORMAT(create_time,'%Y-%m-%d') <= :time and  dept_id in (:deptIds)", nativeQuery = true)
    Integer getUserTotalByCreateTime(@Param(value = "time") String time, @Param(value = "deptIds") Set<Long> deptIds);
}
