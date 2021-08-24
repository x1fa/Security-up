package com.doter.common.security.handler;

import cn.hutool.json.JSONUtil;
import com.doter.common.core.result.Result;
import com.doter.common.enums.ResultStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName AuthAuthenticationEntryPoint
 * @Description 匿名用户访问无权限资源时的异常
 * @Author HanTP
 * @Date 2020/10/13 9:45
 */
@Component
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e){
        log.warn("【未知身份】：{ 业务编码 —— " + ResultStatus.USER_NOT_LOGIN.getCode() + " } —— { 异常信息 —— " + ResultStatus.USER_NOT_LOGIN.getMessage() + " }");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        //200
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        try {
            PrintWriter out = httpServletResponse.getWriter();
            if (e instanceof CredentialsExpiredException) {
                //账号过期
                out.write(JSONUtil.toJsonStr(Result.failed(ResultStatus.USER_NOT_LOGIN,e.getMessage())));
            }else {
                //
                out.write(JSONUtil.toJsonStr(Result.failed(ResultStatus.USER_NOT_LOGIN)));
            }
            out.flush();
            out.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}
