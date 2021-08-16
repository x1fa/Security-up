package com.doter.common.tools;

import java.util.Random;

/**
 * @ClassName UserTool
 * @Description TODO
 * @Author HanTP
 * @Date 2021/3/17 16:08
 */

public class UserTool {

    private static int i = 0;

    /***
     * 获取当前系统时间戳 并截取
     * @return
     */
    private synchronized static String getUnixTime(){
        try {
            Thread.sleep(10);//线程同步执行，休眠10毫秒 防止卡号重复
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i++;i=i>100?i%10:i;
        return ((System.currentTimeMillis()/100)+"").substring(1)+(i%10);
    }

    private static String getRandom() {
        int randomNum = new Random().nextInt(1000000);
        return String.format("%06d", randomNum);
    }

    public synchronized static String createUserName(){
        return getUnixTime();
    }

    public static String createNickName(){
        return "用户"+getRandom();
    }

}
