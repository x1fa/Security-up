package com.doter.common.core.logback;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.doter.common.constant.SecurityConstants;
import com.doter.common.datasource.entity.UserDetail;
import com.doter.common.security.manager.TokenManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * @CLassName LogBackAspect
 * @Description TDD
 * @Author HTP-韩天鹏
 * @Date 2021/7/31 11:48
 * @Version 1.0
 **/
@Aspect
@Component
@Slf4j
public class LogBackAspect {
    private static final String START_TIME = "request-start";

    @Resource
    private TokenManager tokenManager;

    /**
     * 切入点
     */
    @Pointcut(value = "@annotation(com.doter.common.core.logback.annotation.LogBack)")
    public void log() { }

    /**
     * 前置操作
     *
     * @param point 切入点
     */
    @Before("log()")
    public void beforeLog(JoinPoint point) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        String authHeader = request.getHeader(SecurityConstants.TOKEN_HEADER);
        log.info("【请求 URL】：{}", request.getRequestURL().toString());
        log.info("【请求 METHOD】：{}" + request.getMethod());
        log.info("【请求 IP】：{}", request.getRemoteAddr());
        if (StrUtil.isNotBlank(authHeader)){
            UserDetail userDetail = tokenManager.getUserDetail(authHeader.substring(SecurityConstants.TOKEN_PREFIX.length()));
            log.info("【请求 UserId】：{}", userDetail.getId());
            log.info("【请求 UserName】：{}", userDetail.getUsername());
        }
        log.info("【请求类名】：{}，【请求方法名】：{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName());
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("【请求参数】：{}，", JSONUtil.toJsonStr(parameterMap));
        Long start = System.currentTimeMillis();
        request.setAttribute(START_TIME,start);
    }

    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return 原方法返回值
     * @throws Throwable 异常信息
     */
    @Around("log()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();
        log.info("【返回值】：{}", JSONUtil.toJsonStr(result));
        return result;
    }

    /**
     * 后置操作
     */
    @AfterReturning("log()")
    public void afterReturning(JoinPoint point) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        Long start = (Long) request.getAttribute(START_TIME);
        Long end = System.currentTimeMillis();
        log.info("【请求耗时】：{}毫秒", end - start);
    }


}
