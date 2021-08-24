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
/*  认证管理器ProviderManager会调用AuthenticationProvider进行认证
    自定义认证 直接 实现implement AuthenticationProvider 完成认证
 */
public class ImageAuthenticationProvider implements AuthenticationProvider {

    private PasswordEncoder passwordEncoder;

    private UserDetailService userDetailService;

    private CodeManager codeManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //创建image认证对象
        ImageCodeAuthenticationToken imageCodeAuthenticationToken = (ImageCodeAuthenticationToken) authentication;
        //获取用户名
        String principal = (String) imageCodeAuthenticationToken.getPrincipal();
        //密码
        String credentials = (String) imageCodeAuthenticationToken.getCredentials();
        //image redis key
        String key = imageCodeAuthenticationToken.getImageKey();
        //image redis Token
        String code =  imageCodeAuthenticationToken.getImageCode();
        //检验 二维码token
        codeManager.checkImageCode(key,code);
        //给UserDetailSerive 传入用户名 返回用户信息
        UserDetails loadedUser= userDetailService.loadUserByUsername(principal);
        //判断返回的用户信息
        if (loadedUser==null){
            //401, "账号不存在"
            throw new InternalAuthenticationServiceException(ResultStatus.USER_ACCOUNT_NOT_EXIST.getMessage());
        }
        if (!passwordEncoder.matches(credentials,loadedUser.getPassword())){
            //401, "密码错误"
            throw new BadCredentialsException(ResultStatus.USER_CREDENTIALS_ERROR.getMessage());
        }
        if (!loadedUser.isEnabled()){
            //401, "账号被锁定"
            throw new LockedException(ResultStatus.USER_ACCOUNT_LOCKED.getMessage());
        }
        //创建image认证对象，放入用户名和权限信息
        ImageCodeAuthenticationToken userAuth = new ImageCodeAuthenticationToken(loadedUser,loadedUser.getAuthorities());
        //放入 security上下文
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
