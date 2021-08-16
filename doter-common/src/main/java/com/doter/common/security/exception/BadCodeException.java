package com.doter.common.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @ClassName BadCodeException
 * @Description TODO
 * @Author HanTP
 * @Date 2021/3/20 14:41
 */

public class BadCodeException extends AuthenticationException {

    public BadCodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BadCodeException(String msg) {
        super(msg);
    }

}
