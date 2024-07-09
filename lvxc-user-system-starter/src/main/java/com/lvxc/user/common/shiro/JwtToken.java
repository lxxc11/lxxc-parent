package com.lvxc.user.common.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author: Feng
 * @date: 2022年04月20日 18:14
 */
public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}