package com.doter.server.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @ClassName UserDetailService
 * @Description TODO
 * @Author HanTP
 * @Date 2021/7/12 10:03
 */

public interface UserDetailService extends UserDetailsService {

    UserDetails loadUserByPhone(String phone);
}
