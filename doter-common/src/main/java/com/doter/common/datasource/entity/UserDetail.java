package com.doter.common.datasource.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @ClassName UserDetail
 * @Description TODO
 * @Author HanTP
 * @Date 2021/1/7 15:16
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail implements UserDetails {

    /*
     * User(String username, String password,
     * boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
     * Collection<? extends GrantedAuthority> authorities)
     * String username：用户名
     * String password： 密码
     * boolean enabled： 账号是否可用
     * boolean accountNonExpired：账号是否过期
     * boolean credentialsNonExpired：密码是否过期
     * boolean accountNonLocked：账号是否锁定
     * Collection<? extends GrantedAuthority> authorities)：用户权限列表
     */

    private static final long serialVersionUID = 2105363530617880468L;

    private Long id;

    private String username;

    private String phone;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private Byte status;

    private List<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.status==1;
    }
}
