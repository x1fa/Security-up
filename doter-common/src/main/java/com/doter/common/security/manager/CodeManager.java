package com.doter.common.security.manager;

import com.doter.common.constant.SecurityConstants;
import com.doter.common.redis.service.RedisService;
import com.doter.common.security.exception.BadCodeException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName CodeAuthenticationManager
 * @Description TODO
 * @Author HanTP
 * @Date 2021/7/10 11:11
 */
@Component
public class CodeManager {

    @Resource
    private RedisService redisService;

    public void checkImageCode(String codeKey,String code){
        if (!redisService.hasKey(SecurityConstants.RIDES_IMAGE_CODE+codeKey)){
            throw new BadCodeException("验证码已过期");
        }
        String redisCode = redisService.get(SecurityConstants.RIDES_IMAGE_CODE+codeKey).toString();
        if (!code.equals(redisCode)){
            throw new BadCodeException("验证码错误");
        }
        redisService.del(SecurityConstants.RIDES_IMAGE_CODE+codeKey);
    }

    public void checkPhoneCode(String phone,String code){
        if (!redisService.hasKey(SecurityConstants.RIDES_SMS_CODE+phone)){
            throw new BadCodeException("验证码已过期");
        }
        String redisCode = redisService.get(SecurityConstants.RIDES_SMS_CODE+phone).toString();
        if (!code.equals(redisCode)){
            throw new BadCodeException("验证码错误");
        }
        redisService.del(SecurityConstants.RIDES_SMS_CODE+phone);
    }

}
