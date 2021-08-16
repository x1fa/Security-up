package com.doter.common.security.filters;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.doter.common.constant.SecurityConstants;
import com.doter.common.core.result.Result;
import com.doter.common.enums.ResultStatus;
import com.doter.common.redis.service.RedisService;
import com.doter.common.datasource.entity.UserDetail;
import com.doter.common.security.manager.TokenManager;
import com.doter.common.security.properties.IgnoreProperties;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * @ClassName JwtTokenAuthenticationFilter
 * @Description TODO
 * @Author HanTP
 * @Date 2020/11/27 8:44
 */
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private IgnoreProperties ignoreProperties;

    @Resource
    private RedisService redisService;

    @Resource
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String device = httpServletRequest.getHeader(SecurityConstants.DEVICE_HEADER);
        String authHeader = httpServletRequest.getHeader(SecurityConstants.TOKEN_HEADER);
        if (checkUrl(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        if (StrUtil.isNotBlank(authHeader) && authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            String authToken = authHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
            String username = tokenManager.getUserName(authToken);
            if (username == null) {
                response(httpServletResponse, ResultStatus.TOKEN_PARSE_ERROR);
                return;
            }
            String redisKey = SecurityConstants.USER_ACCESS_TOKEN_RIDES + device + ":";
            if (!redisService.hasKey(redisKey + username)) {
                response(httpServletResponse, ResultStatus.TOKEN_EXPIRED);
                return;
            }
            String redisToken = redisService.get(redisKey + username).toString();
            if (!redisToken.equals(authToken)) {
                response(httpServletResponse, ResultStatus.TOKEN_OUT_OF_CTRL);
                return;
            }
            UserDetail userDetail = tokenManager.getUserDetail(authToken);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean checkUrl(HttpServletRequest httpServletRequest) {
        String method = httpServletRequest.getMethod();
        HttpMethod httpMethod = HttpMethod.resolve(method);
        if (ObjectUtil.isNull(httpMethod)) {
            httpMethod = HttpMethod.GET;
        }
        Set<String> ignores = Sets.newHashSet();
        switch (httpMethod) {
            case GET:
                ignores.addAll(ignoreProperties.getGet());
                break;
            case PUT:
                ignores.addAll(ignoreProperties.getPut());
                break;
            case POST:
                ignores.addAll(ignoreProperties.getPost());
                break;
            case DELETE:
                ignores.addAll(ignoreProperties.getDelete());
                break;
            default:
                break;
        }
        ignores.addAll(ignoreProperties.getPattern());
        if (CollUtil.isNotEmpty(ignores)) {
            for (String ignore : ignores) {
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(ignore, method);
                if (matcher.matches(httpServletRequest)) {
                    return true;
                }
            }
        }
        return false;
    }


    private void response(HttpServletResponse httpServletResponse, ResultStatus resultStatus) {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        try {
            PrintWriter out = httpServletResponse.getWriter();
            out.write(JSONUtil.toJsonStr(Result.failed(resultStatus)));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
