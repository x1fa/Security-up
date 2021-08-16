package com.doter.common;

import com.doter.common.redis.service.RedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName AutoConfiguration
 * @Description 扫描注入bean
 * @Author HanTP
 * @Date 2020/5/20 8:51
 */

@ComponentScan("com.doter.common")
public class AutoConfiguration {

    @Bean
    public RedisService redisService(){
        return new RedisService();
    }
}
