package com.general.modules.security.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.io.Serializable;

/**
 * @author L
 * @date 2018-11-23
 * 返回token
 */
@Getter
@AllArgsConstructor
public class AuthenticationInfo implements Serializable {

    private final String token;

    private final JwtUser user;
}
