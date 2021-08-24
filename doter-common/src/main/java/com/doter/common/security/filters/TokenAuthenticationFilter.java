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
        //检查url  【这里可以直接不写，在security 配置就可以】
        if (checkUrl(httpServletRequest)) {
            //直接放行
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        //判断 Authorization是否存在 并且 token 前缀是不是 Bearer
        if (StrUtil.isNotBlank(authHeader) && authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            //获取token
            String authToken = authHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
            //根据token获取用户名
            String username = tokenManager.getUserName(authToken);
            //判断是否为空
            if (username == null) {
                //用户不存在 返回
                response(httpServletResponse, ResultStatus.TOKEN_PARSE_ERROR);
                return;
            }
            //判断不为空 redis的key
            String redisKey = SecurityConstants.USER_ACCESS_TOKEN_RIDES + device + ":";
            //当key不存在
            if (!redisService.hasKey(redisKey + username)) {
                //返回 登录失效 重新登录
                response(httpServletResponse, ResultStatus.TOKEN_EXPIRED);
                return;
            }
            //存在
            //根据rediskey 获取token
            String redisToken = redisService.get(redisKey + username).toString();
            if (!redisToken.equals(authToken)) {
                //rendisToken和authToken 不一致
                //用户在别处登录，请更改密码或重新登录！
                response(httpServletResponse, ResultStatus.TOKEN_OUT_OF_CTRL);
                return;
            }
            //reidsToken和authToken 一致
            //根据Token 获得 user信息
            UserDetail userDetail = tokenManager.getUserDetail(authToken);
            //放入UsernamePasswordAuthenticationToken实体对象
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            //Authentication根据传过去的数据进行封装类型是否一致，一致进行UserdetailsService得到返回数据然后拷贝到Authentication
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //不存在Token直接放行
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean checkUrl(HttpServletRequest httpServletRequest) {
        String method = httpServletRequest.getMethod();
        HttpMethod httpMethod = HttpMethod.resolve(method);
        // 请求方式是否为空
        if (ObjectUtil.isNull(httpMethod)) {
            //空，设置GET
            httpMethod = HttpMethod.GET;
        }
        //不同请求 需要忽略不同url
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
        //需要忽略的 URL 格式，不考虑请求方法
        ignores.addAll(ignoreProperties.getPattern());
        //是不是 不为空
        if (CollUtil.isNotEmpty(ignores)) {
            for (String ignore : ignores) {
                //根据url和method 获取对象
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(ignore, method);
                //判断 对象和当前传入request是否匹配
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
