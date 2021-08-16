package com.doter.common.security.handler;


import cn.hutool.json.JSONUtil;
import com.doter.common.core.result.Result;
import com.doter.common.enums.ResultStatus;
import com.doter.common.security.exception.BadCodeException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName AuthAuthenticationFailureHandler
 * @Description 登录失败处理逻辑
 * @Author HanTP
 * @Date 2020/10/14 14:38
 */
@Component

public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) {
        try {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = httpServletResponse.getWriter();
            if (e instanceof AccountExpiredException) {
                //账号过期
                out.write(JSONUtil.toJsonStr(Result.failed(ResultStatus.USER_ACCOUNT_EXPIRED)));
            } else if (e instanceof BadCredentialsException) {
                //密码错误
                out.write(JSONUtil.toJsonStr(Result.failed(ResultStatus.USER_CREDENTIALS_ERROR)));
            } else if (e instanceof CredentialsExpiredException) {
                //密码过期
                out.write(JSONUtil.toJsonStr(Result.failed(ResultStatus.USER_CREDENTIALS_EXPIRED)));
            } else if (e instanceof DisabledException) {
                //账号不可用
                out.write(JSONUtil.toJsonStr(Result.failed(ResultStatus.USER_ACCOUNT_DISABLE)));
            } else if (e instanceof LockedException) {
                //账号锁定
                out.write(JSONUtil.toJsonStr(Result.failed(ResultStatus.USER_ACCOUNT_LOCKED)));
            } else if (e instanceof InternalAuthenticationServiceException) {
                //用户不存在
                out.write(JSONUtil.toJsonStr(Result.failed(ResultStatus.USER_ACCOUNT_NOT_EXIST)));
            }else if (e instanceof BadCodeException) {
                //用户不存在
                out.write(JSONUtil.toJsonStr(Result.failed(ResultStatus.USER_CREDENTIALS_ERROR,e.getMessage())));
            }else{
                //其他错误
                out.write(JSONUtil.toJsonStr(Result.failed(ResultStatus.ERROR)));
            }
            out.flush();
            out.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}
