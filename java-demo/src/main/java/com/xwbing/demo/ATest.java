package com.xwbing.demo;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2016<br/>
 * 说明: <br/>
 * 项目名称: zxwbing <br/>
 * 创建日期: 2017年3月29日 上午9:19:20 <br/>
 * 作者: xwb
 */

public class ATest {
    private static DecimalFormat df = new DecimalFormat("######0.00");

    public static DateTimeFormatter getDateFormat(String pattern) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);
        return dateFormat;
    }

    public static LocalDateTime nowTimeAfterMinute(long minute) {
        LocalDateTime local = LocalDateTime.now();
        LocalDateTime newDateTime = local.plusMinutes(minute);
        return newDateTime;
    }

    public static String calculationTime(String startTime, String endTime) {
        LocalDateTime sTime = LocalDateTime.parse(startTime, getDateFormat("YYYY-MM-dd HH:mm:ss"));
        LocalDateTime eTime = LocalDateTime.parse(endTime, getDateFormat("YYYY-MM-dd HH:mm:ss"));
        Duration duration = Duration.between(sTime, eTime);
        long h = duration.toHours();
        long m = duration.toMinutes();
        String between = df.format(h + m / 60.0);
        return between;
    }

    public static void main(String[] args) {
        Integer[] ints = {1, 3, 7, 3, 2, 5, 9, 6};
        Arrays.asList(ints).sort((o1, o2) -> o1 - o2);
        System.out.println();
    }
}
