package com.doter.common.exception;


import com.doter.common.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @CLassName GlobalExceptionHandler
 * @Description TDD
 * @Author HTP-韩天鹏
 * @Date 2021/7/31 9:38
 * @Version 1.0
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public Result handleException( Exception e) {
        log.error("全局异常message:" + this.getClass().getPackage().getName() + e.getMessage());
        log.error("全局异常cause:" + this.getClass().getPackage().getName() + e.getCause());
        return Result.failed("全局异常message:" + this.getClass().getPackage().getName() + ":" + e.getMessage());
    }

    @ExceptionHandler({BusinessException.class})
    public Result businessException(BusinessException e){
        // 业务异常
        log.warn("[全局业务异常]业务编码：{"+e.getCode()+"}异常记录：{"+e.getMessage()+"}");
        return Result.failed( e.getCode(),e.getMessage());
    }

}
