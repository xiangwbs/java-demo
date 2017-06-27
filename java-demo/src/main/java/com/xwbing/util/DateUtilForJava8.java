package com.xwbing.util;

import com.xwbing.Exception.BusinessException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 创建时间: 2016/12/8 17:11
 * 作者: xiangwb
 * 说明: 日期处理类
 */
public class DateUtilForJava8 {
    public static DecimalFormat df = new DecimalFormat("######0.00");
    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY = "yyyy";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String HH_MM = "HH:mm";
    /*
     * ChronoUnit:各种时间单位 很好用 | TemporalAdjusters:时态对象 可以获取第一天,最后一天.....|
     * 获取时间分量:Duration要求是localdatetime/localtime类型 | Period要求是localdate类型 | Instant类似于date
     */

    /**
     * 格式化
     *
     * @param pattern
     * @return
     */
    private static DateTimeFormatter getDateFormat(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }

    // //////////////////////基本转换///////////////////////////////基本转换/////////////////////////////////////////

    /**
     * data转string
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String date2Str(Date date, String pattern) {
        Instant instant = date.toInstant();
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime.format(getDateFormat(pattern));
    }

    /**
     * 日期string转date
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date str2Date(String dateStr, String pattern) {//待优化
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusinessException("时间格式转换错误:" + dateStr);
        }
    }

    /**
     * 将毫秒转换为时间字符串
     *
     * @param ms
     * @param pattern
     * @return
     */
    public static String msToDateStr(String ms, String pattern) {
        Instant instant = Instant.ofEpochMilli(Long.valueOf(ms));
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime.format(getDateFormat(pattern));
    }

    /**
     * 将时间戳转换为时间字符串
     *
     * @param s
     * @param pattern
     * @return
     */
    public static String stampToDateStr(String s, String pattern) {
        Instant instant = Instant.ofEpochSecond(Long.valueOf(s));
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime.format(getDateFormat(pattern));
    }

    /**
     * 将时间字符串转为毫秒字符串
     *
     * @param dateStr
     * @return
     */
    public static String dateStrToMs(String dateStr) {
        // TODO: 2017/1/10
        return null;
    }

    /**
     * 将时间字符串转为时间戳
     *
     * @param s
     * @return
     */
    public static String dateStrToStamp(String s) {
        // TODO: 2017/1/10
        return null;
    }

    // ///////////////////////////获取数据////////////获取数据/////////////////////////////////////////////////////////////

    /**
     * 获取周几
     *
     * @param day 0代表当天 负数代表前几天 正数代表后几天
     * @return
     */
    public static String getWeek(int day) {
        LocalDate localDate = LocalDate.now();
        LocalDate date;
        if (day >= 0) {
            date = localDate.plusDays(day);
        } else {
            date = localDate.minusDays(Math.abs(day));
        }
        int week = date.getDayOfWeek().getValue();
        String[] data = {"一", "二", "三", "四", "五", "六", "日"};
        return "周" + data[week - 1];
    }


    /**
     * 获取n分钟前/后时间字符串
     * 返回值為HH_MM_SS
     *
     * @param minute 分钟
     * @return
     */
    public static String nowTimeAddMinusMinutes(int minute) {
        LocalDateTime local = LocalDateTime.now();
        LocalDateTime newDateTime;
        if (minute >= 0) {
            newDateTime = local.plusMinutes(minute);
        } else {
            newDateTime = local.minusMinutes(Math.abs(minute));
        }
        return newDateTime.format(getDateFormat(HH_MM_SS));
    }

    /**
     * 获取n小时前/后的时间字符串
     * 返回格式:hh:mm
     *
     * @param time 格式 hh:mm
     * @param h
     * @return
     */
    public static String timeAddMinusHours(String time, int h) {
        LocalTime localTime = LocalTime.parse(time);
        LocalTime newTime;
        if (h >= 0) {
            newTime = localTime.plusHours(h);
        } else {
            newTime = localTime.minusHours(Math.abs(h));
        }
        return newTime.toString();
    }

    /**
     * 获取n天前/后日期
     * 返回格式：YYYY_MM_DD
     *
     * @param date
     * @param day
     * @return
     */
    public static String dateAddMinusDays(String date, int day) {
        LocalDate localDate = LocalDate.parse(date, getDateFormat(YYYY_MM_DD));
        LocalDate newDate;
        if (day >= 0) {
            newDate = localDate.plusDays(day);
        } else {
            newDate = localDate.minusDays(Math.abs(day));
        }
        return newDate.toString();
    }


    /**
     * 获取当月的第一天
     *
     * @return
     */
    public static String firstDayOfMonth() {
        LocalDate localDate = LocalDate.now();
        LocalDate date = localDate.with(TemporalAdjusters.firstDayOfMonth());
        return date.format(getDateFormat(YYYY_MM_DD));
    }

    /**
     * 获取上个月第一天
     *
     * @return
     */
    public static String firstDayOfLastMonth() {
        LocalDate localDate = LocalDate.now();
        LocalDate date = localDate.minusMonths(1).with(
                TemporalAdjusters.firstDayOfMonth());
        return date.format(getDateFormat(YYYY_MM_DD));
    }

    /**
     * 获取下个月第一天
     *
     * @return
     */
    public static String firstDayOfNextMonth() {
        LocalDate localDate = LocalDate.now();
        LocalDate date = localDate.minusMonths(1).with(
                TemporalAdjusters.firstDayOfMonth());
        return date.format(getDateFormat(YYYY_MM_DD));
    }

    /**
     * 获取指定月份第一天
     *
     * @param month YYYY-MM
     * @return
     */
    public static String getMonthFirstDay(int month) {
        LocalDate localDate = LocalDate.now().withMonth(month)
                .with(TemporalAdjusters.firstDayOfMonth());
        return localDate.format(getDateFormat(YYYY_MM_DD));
    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static String firstDayOfYear() {
        LocalDate localDate = LocalDate.now();
        LocalDate date = localDate.with(TemporalAdjusters.firstDayOfYear());
        return date.format(getDateFormat(YYYY_MM_DD));
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static String getYearFirstDay(int year) {
        LocalDate localDate = LocalDate.now();
        LocalDate date = localDate.withYear(year).with(
                TemporalAdjusters.firstDayOfYear());
        return date.format(getDateFormat(YYYY_MM_DD));
    }

    /**
     * 遍历获取月份集合
     *
     * @param startMoth YYYY-MM
     * @param endMonth  YYYY-MM
     * @return
     * @throws ParseException
     */
    public static List<String> listYearMonth(String startMoth, String endMonth) {
        LocalDate start = LocalDate.parse(startMoth + "-01");
        LocalDate end = LocalDate.parse(endMonth + "-01");
        List<String> list = new ArrayList<>();
        if (startMoth.equals(endMonth)) {
            list.add(startMoth);
            return list;
        }
        long m = ChronoUnit.MONTHS.between(start, end);
        list.add(start.toString());
        for (long i = 1; i <= m; i++) {
            list.add(start.plusMonths(i).format(getDateFormat(YYYY_MM)));
        }
        return list;
    }

    /**
     * 遍历获取两个日期之间天数集合
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static List<String> listDate(String startDate, String endDate) {
        List<String> dateList = new ArrayList<>();
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        long d = ChronoUnit.DAYS.between(start, end);
        dateList.add(start.toString());
        for (long i = 1; i <= d; i++) {
            dateList.add(start.plusDays(i).toString());
        }
        return dateList;
    }

    /////////////////////////////比较////////////相差////////////排序/////////////////////////////////////////////////////////////

    /**
     * 比较时间字符串大小
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean compareDate(String str1, String str2) {
        long longstr1 = Long.valueOf(str1.replaceAll("[-\\s:]", ""));
        long longstr2 = Long.valueOf(str2.replaceAll("[-\\s:]", ""));
        if (longstr1 >= longstr2) {
            return true;
        }
        return false;
    }

    /**
     * 比较两个时间相差几小时（不隔天）
     *
     * @param startTime 09:00
     * @param endTime   13:00
     */
    public static String hoursBetween1(String startTime, String endTime) {
        LocalTime sTime = LocalTime.parse(startTime);
        LocalTime eTime = LocalTime.parse(endTime);
        Duration duration = Duration.between(sTime, eTime);
        long m = duration.toMinutes();
        return df.format(m / 60.0);
    }

    /**
     * 比较两个时间相差几小时（隔天）
     *
     * @param startDateTime 2016-11-11 10:00
     * @param endDateTime   2016-11-12 10:00
     * @return
     */
    public static String hoursBetween2(String startDateTime, String endDateTime) {
        LocalDateTime sDateTime = LocalDateTime.parse(startDateTime);
        LocalDateTime eDateTime = LocalDateTime.parse(endDateTime);
        Duration duration = Duration.between(sDateTime, eDateTime);
        long m = duration.toMinutes();
        return df.format(m / 60.0);
    }

    /**
     * 比较两个日期相差的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long daysBetween(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 比较两个日期相差的月数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long monthBetween(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return ChronoUnit.MONTHS.between(start, end);
    }

    /**
     * 比较两个日期相差的年数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long yearsBetween(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return ChronoUnit.YEARS.between(start, end);
    }

    /**
     * 获取两个时间差(时间分量：日时分秒)
     *
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    public static Map<String, Integer> getDateTimePool(String startDateTime,
                                                       String endDateTime) {
        LocalDateTime sDateTime = LocalDateTime.parse(startDateTime,
                getDateFormat(YYYY_MM_DD_HH_MM_SS));
        LocalDateTime eDateTime = LocalDateTime.parse(endDateTime,
                getDateFormat(YYYY_MM_DD_HH_MM_SS));
        Duration duration = Duration.between(sDateTime, eDateTime);
        long diff = duration.toMillis();
        long diffSeconds = diff / SECOND % 60;
        long diffMinutes = diff / MINUTE % 60;
        long diffHours = diff / HOUR % 24;
        long diffDays = diff / DAY;
        Map<String, Integer> map = new HashMap<>();
        map.put("days", (int) diffDays);
        map.put("hours", (int) diffHours);
        map.put("minutes", (int) diffMinutes);
        map.put("seconds", (int) diffSeconds);
        return map;
    }

    /**
     * 获取两个时间差(时间分量：年月日)
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Map<String, Integer> getDatePool(String startDate, String endDate) {
        LocalDate sDate = LocalDate.parse(startDate);
        LocalDate eDate = LocalDate.parse(endDate);
        Period period = Period.between(sDate, eDate);
        Map<String, Integer> map = new HashMap<>();
        map.put("years", period.getYears());
        map.put("months", period.getMonths());
        map.put("days", period.getDays());
        return map;
    }

    public static void main(String[] args) {
        System.out.println(getDatePool("2017-06-27", "2018-06-29"));
    }

    /**
     * 字符串类型日期集合排序
     *
     * @param list
     * @return
     */
    public static List<String> shortListDate(List<String> list) {
        Collections.sort(list);
        return list;
    }

    /**
     * 判断两者时间是否重合 重合返回true
     *
     * @param startTime 比较的时间段 09:00
     * @param endTime   比较的时间段 13:00
     * @param needSTime 需要的时间段 09:00
     * @param needETime 需要的时间段 13:00
     * @return
     */
    public static boolean compare(String startTime, String endTime,
                                  String needSTime, String needETime) {
        if (needSTime.compareTo(endTime) == 0
                || needETime.compareTo(startTime) == 0) {// 这个表示开始时间等于结束时间,或者结束时间等于开始时间
            return false;
        }
        if (needSTime.compareTo(startTime) >= 0
                && needSTime.compareTo(endTime) < 0) {// 需要时间开始时间在比较时间之间，表示已经重复了
            return true;
        }
        if (needETime.compareTo(startTime) > 0
                && needETime.compareTo(endTime) <= 0) {// 需要时间结束时间在比较时间之间，表示已经重复了
            return true;
        }
        if (needSTime.compareTo(startTime) < 0
                && needETime.compareTo(endTime) > 0) {// 需要时间在比较时间前后，表示已经重复了
            return true;
        }
        return false;
    }
}
