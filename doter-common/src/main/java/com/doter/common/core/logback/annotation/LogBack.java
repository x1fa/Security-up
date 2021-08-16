package com.doter.common.core.logback.annotation;

import java.lang.annotation.*;

/**
 * @CLassName LogBack
 * @Description TDD
 * @Author HTP-韩天鹏
 * @Date 2021/7/31 11:50
 * @Version 1.0
 **/
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogBack {

    /**
     * 模块
     */
    String value() default "";


}
