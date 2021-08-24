package com.doter.common.security.handler;


import cn.hutool.json.JSONUtil;
import com.doter.common.constant.SecurityConstants;
import com.doter.common.core.result.Result;
import com.doter.common.datasource.entity.UserDetail;
import com.doter.common.redis.service.RedisService;
import com.doter.common.security.manager.TokenManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @ClassName AuthAuthenticationSuccessHandler
 * @Description 登录成功处理逻辑
 * @Author HanTP
 * @Date 2020/10/14 15:19
 */
@Component
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private TokenManager tokenManager;

    @Resource
    private RedisService redisService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        try {
            //获取前端数据 device
            String device = httpServletRequest.getHeader(SecurityConstants.DEVICE_HEADER);

            httpServletResponse.setContentType("application/json;charset=utf-8");
            //设置状态码
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            //从认证authentication中获取 userDetail 用户信息
            UserDetail userDetail = (UserDetail) authentication.getPrincipal();
            //根据用户信息 获取 通道tokenKey和刷新tokenKey
            String accessTokenKey = SecurityConstants.USER_ACCESS_TOKEN_RIDES+device+":"+userDetail.getUsername();
            String refreshTokenKey = SecurityConstants.USER_REFRESH_TOKEN_RIDES+device+":"+userDetail.getUsername();
            //写
            PrintWriter out = httpServletResponse.getWriter();
            //根据 用户信息 创建 token
            Map<String,String> map = tokenManager.createToken(userDetail);
            //放到Redis 中
            redisService.set(accessTokenKey,map.get("accessToken"),SecurityConstants.ACCESS_TOKEN_EXPIRATION);
            redisService.set(refreshTokenKey,map.get("refreshToken"),SecurityConstants.REFRESH_TOKEN_EXPIRATION);
            out.write(JSONUtil.toJsonStr(Result.success(map)));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
