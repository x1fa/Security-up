package com.doter.common.security.handler;

import cn.hutool.json.JSONUtil;
import com.doter.common.core.result.Result;
import com.doter.common.enums.ResultStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName AuthAccessDeniedHandler
 * @Description 权限拒绝处理逻辑
 * @Author HanTP
 * @Date 2020/6/20 8:59
 */
@Component
@Slf4j
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) {
        log.warn("【权限不足】：{ 业务编码 —— " + ResultStatus.ACCESS_DENIED.getCode() + " } —— { 异常信息 —— " + ResultStatus.ACCESS_DENIED.getMessage() + " }");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        try {
            PrintWriter out = httpServletResponse.getWriter();
            out.write(JSONUtil.toJsonStr(Result.failed(ResultStatus.ACCESS_DENIED)));
            out.flush();
            out.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}
