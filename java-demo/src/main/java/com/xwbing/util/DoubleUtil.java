package com.xwbing.util;

import java.math.BigDecimal;

/**
 * 浙江卓锐科技股份有限公司 版权所有 Copyright 2017<br/>
 * 说明: <br/>
 * 项目名称: guide_management <br/>
 * 创建日期: 2017/3/11 10:53 <br/>
 * 作者: wdz
 */
public class DoubleUtil {
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 两个Double数相加
     * 
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double add(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2).doubleValue();
    }

    /**
     * 两个Double数相减
     * 
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 两个Double数相乘
     * 
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double mul(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 两个Double数相除
     * 
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double div(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 两个Double数相除，并保留scale位小数
     * 
     * @param v1
     * @param v2
     * @param scale
     * @return Double
     */
    public static Double div(Double v1, Double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 格式化double 四舍五入
     * 
     * @param v1
     * @param scale
     * @return
     */
    public static Double formate(Double v1, int scale) {
        BigDecimal b = new BigDecimal(v1);
        double f1 = b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     * 
     * 功能描述： 不四舍五入保留n位小数 <br/>
     * 作 者：xwb <br/>
     * 创建时间：2016年12月5日 下午6:04:32 <br/>
     * 
     * @param n
     * @param d
     * @return
     */
    public static double decimalND(int n, Double d) { // 不四舍五入
        BigDecimal bg = new BigDecimal(d);
        double re = bg.setScale(n, BigDecimal.ROUND_DOWN).doubleValue();
        return re;
    }

    /**
     * 
     * 功能描述： 保留n位小数 对非零舍弃部分前面的数字加1 <br/>
     * 作 者：xwb <br/>
     * 创建时间：2016年12月5日 下午6:04:32 <br/>
     * 
     * @param n
     * @param d
     * @return
     */
    public static double decimalNU(int n, Double d) {
        BigDecimal bg = new BigDecimal(d);
        double re = bg.setScale(n, BigDecimal.ROUND_UP).doubleValue();
        return re;
    }

}
