package com.doter.server.controller;

import cn.hutool.core.util.IdUtil;
import com.doter.common.constant.SecurityConstants;
import com.doter.common.core.magic.UserMagic;
import com.doter.common.core.result.Result;
import com.doter.common.datasource.entity.UserDetail;
import com.doter.common.datasource.entity.user.DtaUser;
import com.doter.common.datasource.entity.user.DtsUser;
import com.doter.common.enums.ResultStatus;
import com.doter.common.exception.BusinessException;
import com.doter.common.redis.service.RedisService;
import com.doter.common.security.manager.TokenManager;
import com.doter.common.core.logback.annotation.LogBack;
import com.doter.server.service.DtaUserService;
import com.doter.server.service.DtsUserService;
import com.doter.common.tools.ImageTool;
import com.doter.common.tools.SecurityTool;
import com.doter.common.tools.VerifyCodeTool;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @CLassName OAuthController
 * @Description TDD
 * @Author HTP-韩天鹏
 * @Date 2021/8/16 10:31
 * @Version 1.0
 **/
@RestController
@RequestMapping("/oauth")
public class OAuthController {
    @Resource
    private DefaultKaptcha defaultKaptcha;

    @Resource
    private RedisService redisService;

    @Resource
    private DtaUserService dtaUserService;

    @Resource
    private DtsUserService dtsUserService;

    @Resource
    private HttpServletRequest httpServletRequest;

    @Resource
    private TokenManager tokenManager;


    @LogBack
    @GetMapping("/code/image")
    public Result codeByImage() {
        Map<String, Object> map = new HashMap<>();
        try {
            // 生产验证码字符串并保存到rides中
            String createText = defaultKaptcha.createText();
            String captchaKey = "captcha_" + IdUtil.simpleUUID();
            redisService.set(SecurityConstants.RIDES_IMAGE_CODE + captchaKey, createText, (long) (3 * 60));
            // 使用生成的验证码字符串返回一个BufferedImage对象并转为base64
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            String image = ImageTool.toBase64(challenge);
            map.put("image", image);
            map.put("captchaKey", captchaKey);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ResultStatus.BAD_REQUEST, "图片验证码获取失败");
        }
        return Result.success(map);
    }

    @LogBack
    @GetMapping("/code/phone")
    public Result codeByPhone(@RequestParam("phone") String phone) {
        String code = VerifyCodeTool.sixSmsCode();
        redisService.set(SecurityConstants.RIDES_SMS_CODE + phone, code, (long) (15 * 60));
        return Result.success("短信发送成功");
        /*boolean b = smsFeign.verifyCode(phone, code);
        if (b) {

        }*/
//        return b ?  : Result.failed("短信发送失败");
    }

    @LogBack
    @PostMapping("/refreshToken")
    public Result refreshToken(@RequestParam("refreshToken") String refreshToken) {
        String device = httpServletRequest.getHeader(SecurityConstants.DEVICE_HEADER);
        String username = tokenManager.getUserName(refreshToken);
        if (username == null) {
            return Result.failed(ResultStatus.TOKEN_PARSE_ERROR);
        }
        String redisKey = SecurityConstants.USER_REFRESH_TOKEN_RIDES + device + ":";
        if (!redisService.hasKey(redisKey + username)) {
            return Result.failed(ResultStatus.TOKEN_EXPIRED);
        }
        String redisToken = redisService.get(redisKey + username).toString();
        if (!redisToken.equals(refreshToken)) {
            return Result.failed(ResultStatus.TOKEN_OUT_OF_CTRL);
        }
        UserDetail userDetail = tokenManager.getUserDetail(refreshToken);
        String accessTokenKey = SecurityConstants.USER_ACCESS_TOKEN_RIDES + device + ":" + userDetail.getUsername();
        String refreshTokenKey = SecurityConstants.USER_REFRESH_TOKEN_RIDES + device + ":" + userDetail.getUsername();
        Map<String, String> map = tokenManager.createToken(userDetail);
        redisService.set(accessTokenKey, map.get("accessToken"), SecurityConstants.ACCESS_TOKEN_EXPIRATION);
        redisService.set(refreshTokenKey, map.get("refreshToken"), SecurityConstants.REFRESH_TOKEN_EXPIRATION);
        return Result.success(map);
    }




    @LogBack
    @GetMapping("/{type}/userInfo")
    public Result userInfo(@PathVariable("type") String userType) {
        UserDetail userDetail = SecurityTool.getUser();
        if (UserMagic.USER_TYPE_ADMIN.equals(userType)) {
            DtsUser dtsUser = dtsUserService.getById(userDetail.getId());
            return Result.success(dtsUser);
        } else {
            DtaUser dtaUser = dtaUserService.getById(userDetail.getId());
            return Result.success(dtaUser);
        }
    }
}
