package com.doter.common.tools;

class AESToolTest {
    public static void main(String[] args) {
        String encrypt = AESTool.encrypt(123);
        System.out.println(encrypt);
    }

}
