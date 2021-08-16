package com.doter.common.constant;

public interface SecurityConstants {

    String RIDES_IMAGE_CODE="verification:code:image:";

    String RIDES_SMS_CODE="verification:code:mobile:";

    String USER_ACCESS_TOKEN_RIDES="user_auth:token:access:";

    String USER_REFRESH_TOKEN_RIDES="user_auth:token:refresh:";


    String DEVICE_MOBILE = "mobile";

    String DEVICE_COMPUTER = "computer";


    /**
     * JWT令牌前缀
     */
    String TOKEN_PREFIX = "Bearer ";
    /**
     * 认证信息Http请求头
     */
    String TOKEN_HEADER = "Authorization";

    String DEVICE_HEADER = "device";

    String TOKEN_SECRET="transport.yun.tong.wu.liu";

    Long ACCESS_TOKEN_EXPIRATION= (long) (60*60*24*7);

    Long REFRESH_TOKEN_EXPIRATION = (long) (60*60*24*14);

}