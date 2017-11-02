package com.xwbing.util;

import java.util.Random;

/**
 * 说明:生成随机码
 * 创建日期: 2016年11月29日 上午10:28:21
 * 作者: xiangwb
 */
public class RadomUtil {

    private static final String STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String buildRandom(int length) {
        if (length < 1)
            throw new RuntimeException("参数异常!!!");
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(STR.length());
            sb.append(STR.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(buildRandom(8));
    }
}
