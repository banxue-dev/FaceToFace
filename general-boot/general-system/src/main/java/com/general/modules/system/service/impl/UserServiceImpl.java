package com.general.modules.system.service.impl;

import com.general.exception.EntityExistException;
import com.general.exception.EntityNotFoundException;
import com.general.modules.system.domain.User;
import com.general.modules.system.domain.UserAvatar;
import com.general.modules.system.service.DeptService;
import com.general.modules.system.service.UserService;
import com.general.modules.system.service.dto.DeptDTO;
import com.general.modules.system.service.dto.RoleSmallDTO;
import com.general.modules.system.service.dto.UserDTO;
import com.general.modules.system.service.dto.UserQueryCriteria;
import com.general.modules.system.service.mapper.UserMapper;
import com.general.utils.*;
import com.general.modules.monitor.service.RedisService;
import com.general.modules.system.repository.UserAvatarRepository;
import com.general.modules.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author L
 * @date 2018-11-23
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserAvatarRepository userAvatarRepository;

    @Autowired
    private DeptService deptService;

    @Value("${file.avatar}")
    private String avatar;

    @Override
    public Object queryAll(UserQueryCriteria criteria, Pageable pageable) {
        Page<User> page = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(userMapper::toDto));
    }

    @Override
    public List<UserDTO> queryAll(UserQueryCriteria criteria) {
        List<User> users = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        return userMapper.toDto(users);
    }

    @Override
    public UserDTO findById(long id) {
        Optional<User> user = userRepository.findById(id);
        ValidationUtil.isNull(user,"User","id",id);
        return userMapper.toDto(user.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO create(User resources) {

        //验证组织人员是否已满
        Long deptId = resources.getDept().getId();
        //查询组织信息
        DeptDTO dept = deptService.findById(deptId);
        //查询组织下的人数
        if(userRepository.findByUsername(resources.getUsername())!=null){
            throw new EntityExistException(User.class,"username",resources.getUsername());
        }

//        if(userRepository.findByEmail(resources.getEmail())!=null){
//            throw new EntityExistException(User.class,"email",resources.getEmail());
//        }

        // 默认密码 123456，此密码是加密后的字符
        if(StringUtils.isNotEmpty(resources.getPassword())){
            resources.setPassword(EncryptUtils.encryptPassword(resources.getPassword()));
        }else {
            resources.setPassword(EncryptUtils.encryptPassword("123456"));
        }
        return userMapper.toDto(userRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User resources) {
        Optional<User> userOptional = userRepository.findById(resources.getId());
        ValidationUtil.isNull(userOptional,"User","id",resources.getId());
        User user = userOptional.get();
        User user1 = userRepository.findByUsername(user.getUsername());
//        User user2 = userRepository.findByEmail(user.getEmail());
        if(user1 !=null&&!user.getId().equals(user1.getId())){
            throw new EntityExistException(User.class,"username",resources.getUsername());
        }
//        if(user2!=null&&!user.getId().equals(user2.getId())){
//            throw new EntityExistException(User.class,"email",resources.getEmail());
//        }
        // 如果用户的角色改变了，需要手动清理下缓存
        if (!resources.getRoles().equals(user.getRoles())) {
            String key = "role::loadPermissionByUser:" + user.getUsername();
            redisService.delete(key);
            key = "role::findByUsers_Id:" + user.getId();
            redisService.delete(key);
        }
        
        if(StringUtils.isNotEmpty(resources.getPassword())){
        	user.setPassword(EncryptUtils.encryptPassword(resources.getPassword()));
        }
        user.setName(resources.getName());
        user.setUsername(resources.getUsername());
//        user.setEmail(resources.getEmail());
        user.setEnabled(resources.getEnabled());
        user.setUserType(resources.getUserType());
        user.setServiceTime(resources.getServiceTime());
        user.setRoles(resources.getRoles());
        user.setDept(resources.getDept());
        user.setChannels(resources.getChannels());
        user.setChannelsSet(resources.getChannelsSet());
        user.setEnterpriseCode(resources.getEnterpriseCode());
        user.setLevel(resources.getLevel());
        user.setLocationInterval(resources.getLocationInterval());
        user.setLocationSwitch(resources.getLocationSwitch());
        user.setVideoSwitch(resources.getVideoSwitch());
//        user.setPhone(resources.getPhone());
        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO findByName(String userName) {
        User user = null;
        if(ValidationUtil.isEmail(userName)){
            user = userRepository.findByEmail(userName);
        } else {
            user = userRepository.findByUsername(userName);
        }
        if (user == null) {
            throw new EntityNotFoundException(User.class, "name", userName);
        } else {
            return userMapper.toDto(user);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String pass) {
        userRepository.updatePass(username,pass,new Date());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLocationSwitch(Long id, Integer status) {
        userRepository.updateLocationSwitch(id,status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVideoSwitch(Long id, Integer status) {
        userRepository.updateVideoSwitch(id,status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(MultipartFile multipartFile) {
        User user = userRepository.findByUsername(SecurityUtils.getUsername());
        UserAvatar userAvatar = user.getUserAvatar();
        String oldPath = "";
        if(userAvatar != null){
           oldPath = userAvatar.getPath();
        }
        File file = FileUtil.upload(multipartFile, avatar);
        userAvatar = userAvatarRepository.save(new UserAvatar(userAvatar,file.getName(), file.getPath(), FileUtil.getSize(multipartFile.getSize())));
        user.setUserAvatar(userAvatar);
        userRepository.save(user);
        if(StringUtils.isNotBlank(oldPath)){
            FileUtil.del(oldPath);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        userRepository.updateEmail(username,email);
    }

    @Override
    public void download(List<UserDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDTO userDTO : queryAll) {
            List roles = userDTO.getRoles().stream().map(RoleSmallDTO::getName).collect(Collectors.toList());
            Map map = new LinkedHashMap();
            map.put("用户名", userDTO.getUsername());
            map.put("头像", userDTO.getAvatar());
            map.put("邮箱", userDTO.getEmail());
            map.put("状态", userDTO.getEnabled() ? "启用" : "禁用");
            map.put("手机号码", userDTO.getPhone());
            map.put("角色", roles);
            map.put("组织", userDTO.getDept().getName());
            map.put("最后修改密码的时间", userDTO.getLastPasswordResetTime());
            map.put("创建日期", userDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
