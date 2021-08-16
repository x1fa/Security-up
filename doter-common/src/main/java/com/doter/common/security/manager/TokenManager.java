package com.doter.common.security.manager;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.doter.common.constant.SecurityConstants;
import com.doter.common.datasource.entity.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TokenProvider
 * @Description TODO
 * @Author HanTP
 * @Date 2021/6/7 15:17
 */
@Slf4j
@Component
public class TokenManager {

    private static final String CLAIM_KEY_USER = "user";
    private static final String CLAIM_KEY_AUTH = "auth";
    private static final String CLAIM_KEY_TYPES = "types";

    public Map<String, String> createToken(UserDetail userDetail){
        String accessToken = createAccessToken(userDetail);
        String refreshToken = createRefreshToken(userDetail);
        Map<String,String> map = new HashMap<>(2);
        map.put("accessToken",accessToken);
        map.put("refreshToken",refreshToken);
        return map;
    }

    /**
     * 创建JWT
     *
     * @param userDetail 用户认证信息
     * @return JWT
     */
    public String createAccessToken(UserDetail userDetail) {
        Map<String, Object> claims = new HashMap<>(3);
        claims.put(CLAIM_KEY_USER, userDetail);
        claims.put(CLAIM_KEY_AUTH, userDetail.getAuthorities());
        claims.put(CLAIM_KEY_TYPES,"access");
        return createToken(userDetail.getId(), userDetail.getUsername(),
                SecurityConstants.ACCESS_TOKEN_EXPIRATION, claims);
    }

    public String createRefreshToken(UserDetail userDetail) {
        Map<String, Object> claims = new HashMap<>(3);
        claims.put(CLAIM_KEY_USER, userDetail);
        claims.put(CLAIM_KEY_AUTH, userDetail.getAuthorities());
        claims.put(CLAIM_KEY_TYPES,"refresh");
        return createToken(userDetail.getId(), userDetail.getUsername(),
                SecurityConstants.REFRESH_TOKEN_EXPIRATION, claims);
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUserName(String token) {
        String username;
        try {
            Claims claims = parseToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 判断token是否已经失效
     */
    public boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    public UserDetail getUserDetail(String token) {
        Claims claims = this.parseToken(token);
        JSONObject jsonObject = JSONUtil.parseObj(claims.get(CLAIM_KEY_USER));
        return JSONUtil.toBean(jsonObject,UserDetail.class);
    }

    /**
     * 根据负责生成JWT的token
     */
    private String createToken(Long userId, String userName, Long expirationTime, Map<String, Object> claims) {
        //这里其实就是new一个JwtBuilder，设置jwt的body
        return Jwts.builder()
                //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，
                // 一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，
                // 这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(userId.toString())
                //sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，
                // 可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setSubject(userName)
                //iat: jwt的签发时间
                .setIssuedAt(new Date())
                //设置过期时间
                .setExpiration(expiration(expirationTime))
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(SignatureAlgorithm.HS512, Base64.encode(SecurityConstants.TOKEN_SECRET))
                .compact();

    }

    /**
     * 生成token的过期时间
     */
    private Date expiration(Long expirationTime) {
        return new Date(System.currentTimeMillis() + expirationTime * 1000);
    }
    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims parseToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(Base64.encode(SecurityConstants.TOKEN_SECRET))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.info("Token格式验证失败:{}", token);
        }
        return claims;
    }



}
