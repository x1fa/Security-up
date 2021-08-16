package com.doter.common.tools;

import java.util.Random;

/**
 * @ClassName VerifyCodeTool
 * @Description TODO
 * @Author HanTP
 * @Date 2021/5/24 22:41
 * @Version
 */
public class VerifyCodeTool {

    public static String sixSmsCode(){
        int randomNum = new Random().nextInt(1000000);
        return String.format("%06d", randomNum);
    }
}
