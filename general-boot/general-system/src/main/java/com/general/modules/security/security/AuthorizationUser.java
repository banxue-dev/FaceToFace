package com.general.modules.security.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author L
 * @date 2018-11-30
 */
@Getter
@Setter
public class AuthorizationUser {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String code;

    private String uuid = "";

    @Override
    public String toString() {
        return "{username=" + username  + ", password= ******}";
    }
}
