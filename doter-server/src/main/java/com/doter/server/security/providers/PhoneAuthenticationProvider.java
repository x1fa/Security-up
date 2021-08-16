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
        PhoneCodeAuthenticationToken phoneCodeAuthenticationToken = (PhoneCodeAuthenticationToken) authentication;
        String principal = (String) phoneCodeAuthenticationToken.getPrincipal();
        String credentials = (String) phoneCodeAuthenticationToken.getCredentials();
        codeManager.checkPhoneCode(principal,credentials);
        UserDetails loadedUser = userDetailService.loadUserByPhone(principal);
        if (loadedUser==null){
            throw new InternalAuthenticationServiceException(ResultStatus.USER_ACCOUNT_NOT_EXIST.getMessage());
        }
        if (!loadedUser.isEnabled()){
            throw new LockedException(ResultStatus.USER_ACCOUNT_LOCKED.getMessage());
        }
        PhoneCodeAuthenticationToken userAuth = new PhoneCodeAuthenticationToken(loadedUser,loadedUser.getAuthorities());
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
