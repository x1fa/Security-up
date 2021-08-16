package com.doter.common.tools;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

/**
 * @CLassName AESTool
 * @Description TDD
 * @Author HTP-韩天鹏
 * @Date 2021/8/16 9:27
 * @Version 1.0
 **/

public class AESTool {
    /**
     * key：必须16个字符，且要和前端保持一致
     */
    private final static String KEY = "YUNTONGJMCODEKEY";
    /**
     * 偏移量：必须16个字符，且要和前端保持一致
     */
    private final static String IV = "YUNTONGPYLCODEIV";


    /**
     * 加密返回的数据转换成 String 类型
     *
     * @param content 明文
     */
    public static String encrypt(String content) {
        return parseByte2HexStr(Objects.requireNonNull(aesCbcEncrypt(content.getBytes(), KEY.getBytes(), IV.getBytes())));
    }

    /**
     * 将解密返回的数据转换成 String 类型
     *
     * @param content Base64编码的密文
     */
    public static String decrypt(String content) {
        return new String(Objects.requireNonNull(aesCbcDecrypt(parseHexStr2Byte(content), KEY.getBytes(), IV.getBytes())));
    }

    private static byte[] aesCbcEncrypt(byte[] content, byte[] keyBytes, byte[] iv) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            //设置模式，编码，后端为PKCS5Padding，对应前端是Pkcs7
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            return cipher.doFinal(content);
        } catch (Exception e) {
            System.out.println("exception:" + e.toString());
        }
        return null;
    }

    private static byte[] aesCbcDecrypt(byte[] content, byte[] keyBytes, byte[] iv) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return cipher.doFinal(content);
        } catch (Exception e) {
            System.out.println("exception:" + e.toString());
        }
        return null;
    }

    /**
     * 将byte数组转换成16进制String
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制String转换为byte数组
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
