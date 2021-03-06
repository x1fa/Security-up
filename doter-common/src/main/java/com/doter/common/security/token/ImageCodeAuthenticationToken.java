package com.doter.common.security.token;



import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @ClassName ImageCodeAuthenticationToken
 * @Description TODO
 * @Author HanTP
 * @Date 2020/11/26 16:35
 */

public class ImageCodeAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = -446473940300005668L;

    private final Object principal;
    private final Object credentials;
    private final String imageKey;
    private final String imageCode;
    /**
     * 认证成功前
     * @param
     */
    //实体类 不同参数的构造函数
    public ImageCodeAuthenticationToken(Object principal, Object credentials, String imageKey, String imageCode) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.imageKey = imageKey;
        this.imageCode = imageCode;
        //传入的用户名密码，redisKey 和 token 不通过认证
        //设置不通过认证
        super.setAuthenticated(false);
    }

    /**
     * 认证成功后
     * @param principal
     * @param authorities
     *
     */
    public ImageCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = null;
        this.imageKey = null;
        this.imageCode = null;
        //传入用户信息和权限列表 通过认证
        //设置通过认证
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

    public String getImageKey() {
        return imageKey;
    }

    public String getImageCode() {
        return imageCode;
    }
}
