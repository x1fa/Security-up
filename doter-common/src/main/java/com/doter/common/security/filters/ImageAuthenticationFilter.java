package com.doter.common.security.filters;

import cn.hutool.core.util.StrUtil;
import com.doter.common.tools.AESTool;
import com.doter.common.security.token.ImageCodeAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName AdminPasswordAuthenticationFilter
 * @Description TODO
 * @Author HanTP
 * @Date 2021/6/12 23:42
 * @Version
 */
public class ImageAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String IMAGE_KEY = "imageKey";
    public static final String IMAGE_CODE = "imageCode";
    private String usernameParam = USERNAME;
    private String passwordParam = PASSWORD;
    private String imageKeyParam = IMAGE_KEY;
    private String imageCodeParam = IMAGE_CODE;


    public ImageAuthenticationFilter(String path){
        super(new AntPathRequestMatcher(path,"POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        if (!"POST".equals(httpServletRequest.getMethod())) {
            //不是POST请求直接返回 不支持
            throw new AuthenticationServiceException(
                    "不支持的身份验证方法: " + httpServletRequest.getMethod());
        }
        //前端获取数据
        String userType = httpServletRequest.getParameter("userType");
        String username = httpServletRequest.getParameter(usernameParam).trim();
        String password = httpServletRequest.getParameter(passwordParam);
        String key = httpServletRequest.getParameter(imageKeyParam);
        String code = httpServletRequest.getParameter(imageCodeParam).trim();
        //判断是否为空
        if (StrUtil.isBlank(userType)){
            throw new AuthenticationServiceException("用户类型不能为空！");
        }
        if (StrUtil.isBlank(username)){
            throw new AuthenticationServiceException("用户名不能为空！");
        }
        if (StrUtil.isBlank(password)){
            throw new AuthenticationServiceException("密码不能为空！");
        }
        if (StrUtil.isBlank(code)){
            throw new AuthenticationServiceException("验证码不能为空！");
        }
        //放入实体对象
        ImageCodeAuthenticationToken imageCodeAuthenticationToken = new ImageCodeAuthenticationToken(username, AESTool.decrypt(password),
                key,code);
        this.setDetails(httpServletRequest,imageCodeAuthenticationToken);
        //去ProviderManager 认证提供者管理中认证
        return this.getAuthenticationManager().authenticate(imageCodeAuthenticationToken);
    }

    private void setDetails(HttpServletRequest request, ImageCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public final String getUsernameParam() {
        return usernameParam;
    }

    public void setUsernameParameter(String usernameParam) {
        Assert.hasText(usernameParam, "用户名参数不能为空");
        this.usernameParam = usernameParam;
    }

    public final String getPasswordParam() {
        return passwordParam;
    }

    public void setPasswordParam(String passwordParam) {
        Assert.hasText(passwordParam, "密码参数不能为空");
        this.passwordParam = passwordParam;
    }

    public String getImageKeyParam() {
        return imageKeyParam;
    }

    public void setImageKeyParam(String imageKeyParam) {
        Assert.hasText(imageKeyParam, "密匙参数不能为空");
        this.imageKeyParam = imageKeyParam;
    }

    public String getImageCodeParam() {
        return imageCodeParam;
    }

    public void setImageCodeParam(String imageCodeParam) {
        Assert.hasText(imageCodeParam, "验证码参数不能为空");
        this.imageCodeParam = imageCodeParam;
    }
}
