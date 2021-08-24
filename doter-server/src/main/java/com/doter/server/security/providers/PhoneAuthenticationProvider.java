package com.doter.server.security.providers;


import com.doter.common.enums.ResultStatus;
import com.doter.common.security.manager.CodeManager;
import com.doter.common.security.token.PhoneCodeAuthenticationToken;
import com.doter.server.service.UserDetailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @ClassName
 * @Description TODO
 * @Author HanTP
 * @Date 2021/6/13 12:52
 */

public class PhoneAuthenticationProvider implements AuthenticationProvider {


    private UserDetailService userDetailService;

    private CodeManager codeManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //强转类型 创建认证对象
        PhoneCodeAuthenticationToken phoneCodeAuthenticationToken = (PhoneCodeAuthenticationToken) authentication;
        //获取前端数据
        String principal = (String) phoneCodeAuthenticationToken.getPrincipal();
        String credentials = (String) phoneCodeAuthenticationToken.getCredentials();
        //手机验证码
        codeManager.checkPhoneCode(principal,credentials);
        //查询数据库
        UserDetails loadedUser = userDetailService.loadUserByPhone(principal);
        //判断
        if (loadedUser==null){
            throw new InternalAuthenticationServiceException(ResultStatus.USER_ACCOUNT_NOT_EXIST.getMessage());
        }
        if (!loadedUser.isEnabled()){
            throw new LockedException(ResultStatus.USER_ACCOUNT_LOCKED.getMessage());
        }
        //创建认证对象 放入用户信息和权限信息
        PhoneCodeAuthenticationToken userAuth = new PhoneCodeAuthenticationToken(loadedUser,loadedUser.getAuthorities());
        //放入security上下文
        SecurityContextHolder.getContext().setAuthentication(userAuth);
        return userAuth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return PhoneCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }

    public UserDetailService getUserDetailService() {
        return userDetailService;
    }

    public void setUserDetailService(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    public CodeManager getCodeManager() {
        return codeManager;
    }

    public void setCodeManager(CodeManager codeManager) {
        this.codeManager = codeManager;
    }
}
