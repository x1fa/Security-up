package com.doter.server.security.providers;

import com.doter.common.enums.ResultStatus;
import com.doter.common.security.manager.CodeManager;
import com.doter.common.security.token.ImageCodeAuthenticationToken;
import com.doter.server.service.UserDetailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ClassName
 * @Description TODO
 * @Author HanTP
 * @Date 2021/6/13 10:48
 */

public class ImageAuthenticationProvider implements AuthenticationProvider {

    private PasswordEncoder passwordEncoder;

    private UserDetailService userDetailService;

    private CodeManager codeManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ImageCodeAuthenticationToken imageCodeAuthenticationToken = (ImageCodeAuthenticationToken) authentication;
        String principal = (String) imageCodeAuthenticationToken.getPrincipal();
        String credentials = (String) imageCodeAuthenticationToken.getCredentials();
        String key = imageCodeAuthenticationToken.getImageKey();
        String code =  imageCodeAuthenticationToken.getImageCode();
        codeManager.checkImageCode(key,code);
        UserDetails loadedUser= userDetailService.loadUserByUsername(principal);
        if (loadedUser==null){
            throw new InternalAuthenticationServiceException(ResultStatus.USER_ACCOUNT_NOT_EXIST.getMessage());
        }
        if (!passwordEncoder.matches(credentials,loadedUser.getPassword())){
            throw new BadCredentialsException(ResultStatus.USER_CREDENTIALS_ERROR.getMessage());
        }
        if (!loadedUser.isEnabled()){
            throw new LockedException(ResultStatus.USER_ACCOUNT_LOCKED.getMessage());
        }
        ImageCodeAuthenticationToken userAuth = new ImageCodeAuthenticationToken(loadedUser,loadedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(userAuth);
        return userAuth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ImageCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
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
