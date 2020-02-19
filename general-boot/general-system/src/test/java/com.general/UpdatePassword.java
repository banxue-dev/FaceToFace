package com.general;

import com.general.modules.system.service.UserService;
import com.general.utils.EncryptUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description:
 * @Author LuoJing
 * @Date 2019/10/2918:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdatePassword {

    @Autowired
    private UserService userService;

    @Test
    public void test(){
        userService.updatePass("admin", EncryptUtils.encryptPassword("123456"));
    }

}
