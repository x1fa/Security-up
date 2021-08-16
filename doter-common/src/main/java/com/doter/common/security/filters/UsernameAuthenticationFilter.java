package com.doter.common.security.filters;

import cn.hutool.core.util.StrUtil;
import com.doter.common.tools.AESTool;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName UsernameAuthenticationFilter
 * @Description TODO
 * @Author HanTP
 * @Date 2021/7/13 9:24
 */

public class UsernameAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public UsernameAuthenticationFilter(String path) {
        super(new AntPathRequestMatcher(path, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "不支持的身份验证方法: " + request.getMethod());
        }

        String userType = request.getParameter("userType");
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password");
        if (StrUtil.isBlank(userType)) {
            throw new AuthenticationServiceException("用户类型不能为空！");
        }
        if (StrUtil.isBlank(username)) {
            throw new AuthenticationServiceException("用户名不能为空！");
        }
        if (StrUtil.isBlank(password)) {
            throw new AuthenticationServiceException("密码不能为空！");
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, AESTool.decrypt(password));
        this.setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);

    }

    private void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
