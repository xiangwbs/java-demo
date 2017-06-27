package com.xwbing.util;

/**
 * 说明: 容量单位换算
 * 创建日期: 2016年12月6日 下午3:20:58
 * 作者: xwb
 */

public class GMKBUtil {
    /**
     * 容量单位换算成B KB MB GB
     *
     * @param size
     * @return
     */
    public static String convertCapacity(double size) {
        double kb = 1024;
        double mb = kb * 1024;
        double gb = mb * 1024;
        if (size >= gb) {
            size = size / gb;
            return String.format("%.2f GB", size);//四舍五入保留2位小数
        } else if (size >= mb) {
            size = size / mb;
            return String.format("%.2f MB", size);
        } else if (size >= kb) {
            size = size / kb;
            return String.format("%.2f KB", size);
        } else
            return String.format("%f B", size);
    }

    public static void main(String[] args) {
        double d = 10241024.0;
        System.out.println(convertCapacity(d));
    }
}
