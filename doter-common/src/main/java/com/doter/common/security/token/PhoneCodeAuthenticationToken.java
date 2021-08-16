package com.doter.common.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @ClassName ImageCodeAuthenticationToken
 * @Description TODO
 * @Author HanTP
 * @Date 2020/10/26 15:51
 */

public class PhoneCodeAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 8444153911683312653L;
    private final Object principal;
    private final Object credentials;
    /**
     * 认证成功前
     * @param
     */
    public PhoneCodeAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(false);
    }

    /**
     * 认证成功后
     * @param principal
     * @param authorities
     *
     */
    public PhoneCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = null;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

}
