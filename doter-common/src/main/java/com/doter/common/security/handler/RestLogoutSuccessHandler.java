package com.doter.common.security.handler;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.doter.common.constant.SecurityConstants;
import com.doter.common.core.result.Result;
import com.doter.common.redis.service.RedisService;
import com.doter.common.security.manager.TokenManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName AuthLogoutSuccessHandler
 * @Description 登出成功处理逻辑
 * @Author HanTP
 * @Date 2020/10/14 15:37
 */
@Component
public class RestLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private TokenManager tokenManager;

    @Resource
    private RedisService redisService;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication){
        try {
            String device = httpServletRequest.getHeader(SecurityConstants.DEVICE_HEADER);
            httpServletResponse.setContentType("application/json;charset=utf-8");
            //200
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            //获取 请求头Authorization
            String authHeader = httpServletRequest.getHeader(SecurityConstants.TOKEN_HEADER);

            if (StrUtil.isNotBlank(authHeader) && authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                //不为空 获取token
                String authToken = authHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
                //根据token获取 用户名
                String username = tokenManager.getUserName(authToken);
                //redis 删除刷新token 和 通道token
                redisService.del(SecurityConstants.USER_ACCESS_TOKEN_RIDES+device+":"+username,
                        SecurityConstants.USER_REFRESH_TOKEN_RIDES+device+":"+username);
            }
            //为空
            PrintWriter out = httpServletResponse.getWriter();
            out.write(JSONUtil.toJsonStr(Result.success(null,"退出成功！")));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
