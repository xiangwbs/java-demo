package com.xwbing.util;

/**
 * 说明: 自定义StringUtils
 * 项目名称: spring-demo
 * 创建时间: 2017/6/12 13:28
 * 作者: xiangwb
 */
public class MyStringUtils {
    public static boolean isEmpty(Object... strings) {
        for (Object string : strings) {
            if (string == null || "".equals(string) || "null".equals(string)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEmpty(Object... strings) {
        return !isEmpty(strings);
    }
}

