package com.doter.common.security.filters;

import cn.hutool.core.util.StrUtil;
import com.doter.common.security.token.PhoneCodeAuthenticationToken;
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
 * @ClassName AdminPhoneAuthenticationFilter
 * @Description TODO
 * @Author HanTP
 * @Date 2021/6/12 23:46
 * @Version
 */
public class PhoneAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String PHONE_NUMBER = "phone";
    public static final String PHONE_CODE = "code";
    private String phoneNumberParam = PHONE_NUMBER;
    private String phoneCodeParam = PHONE_CODE;

    public PhoneAuthenticationFilter(String path){
        super(new AntPathRequestMatcher(path,"POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if (!"POST".equals(httpServletRequest.getMethod())) {
            //不是POST请求方法直接返回 不支持的身份验证方法
            throw new AuthenticationServiceException(
                    "不支持的身份验证方法: " + httpServletRequest.getMethod());
        }
        //获取前端传入数据
        String userType = httpServletRequest.getParameter("userType");
        String phone = httpServletRequest.getParameter(phoneNumberParam).trim();
        String code = httpServletRequest.getParameter(phoneCodeParam).trim();
        //判断是否为空
        if (StrUtil.isBlank(userType)){
            throw new AuthenticationServiceException("用户类型不能为空！");
        }
        if (StrUtil.isBlank(phone)){
            throw new AuthenticationServiceException("用户名不能为空！");
        }
        if (StrUtil.isBlank(code)){
            throw new AuthenticationServiceException("验证码不能为空！");
        }
        //放入放入实体对象
        PhoneCodeAuthenticationToken phoneCodeAuthenticationToken = new PhoneCodeAuthenticationToken(phone,code);
        this.setDetails(httpServletRequest,phoneCodeAuthenticationToken);
        //在ProviderManager 认证提供者管理器中 进行认证
        return this.getAuthenticationManager().authenticate(phoneCodeAuthenticationToken);
    }

    private void setDetails(HttpServletRequest request, PhoneCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public String getPhoneNumberParam() {
        return phoneNumberParam;
    }

    public void setPhoneNumberParam(String phoneNumberParam) {
        Assert.hasText(phoneNumberParam, "手机号参数不能为空");
        this.phoneNumberParam = phoneNumberParam;
    }

    public String getPhoneCodeParam() {
        return phoneCodeParam;
    }

    public void setPhoneCodeParam(String phoneCodeParam) {
        Assert.hasText(phoneCodeParam, "验证码参数不能为空");
        this.phoneCodeParam = phoneCodeParam;
    }

}
