package com.xwbing.demo;

import com.xwbing.entity.SysUser;
import com.xwbing.service.sys.SysUserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Date: 2017/6/15 17:09
 * Author: xiangwb
 * Project: zdemo
 * Description: java8新特性
 */
public class Java8Demo {
    public static void main(String[] args) {
        //匿名内部类
        Thread t = new Thread(() -> System.out.println("hello,lambda"));
        t.start();
        List<String> list = Arrays.asList("a", "b", "c");
        Collections.sort(list, (str1, str2) -> str1.compareTo(str2));

        List<Integer> lists = Arrays.asList(1, 2, 4, 2, 3, 5, 5, 6, 7, 8, 9, 10);
        //stream api 高级版本的迭代器
//        Arrays.stream(arrays).forEach(System.out::println);//遍历
//        lists.forEach(System.out::println);//遍历
        System.out.println("sort:" + lists.stream().sorted((o1, o2) -> o2 - o1).collect(Collectors.toList()));//排序
        System.out.println("conver:" + lists.stream().map(o1 -> o1 * 2).collect(Collectors.toList()));//转换
        //过滤
        Predicate<Integer> boolValue = x -> x > 4;
        System.out.println("filter:" + lists.stream().filter(o1 -> o1 > 4).collect(Collectors.toList()));
        System.out.println("filter:" + lists.stream().filter(boolValue).collect(Collectors.toList()));

        System.out.println("distinct:" + lists.stream().distinct().collect(Collectors.toList()));//去重
        System.out.println("limit:" + lists.stream().limit(4).collect(Collectors.toList()));//截取
        System.out.println("skip:" + lists.stream().skip(4).collect(Collectors.toList()));//丢弃
        //reduce
        System.out.println("reduce:" + lists.stream().reduce((o1, o2) -> o1 + o2).get());//聚合
        System.out.println("reduce:" + lists.stream().reduce(0, (o1, o2) -> o1 + o2));//聚合(给定默认值)
        System.out.println("ids:" + list.stream().reduce((sum, item) -> sum + "," + item).get());//list(a,b,c)-->,a,b,c-->a,b,c
        System.out.println("ids:" + list.stream().reduce("", (sum, item) -> sum + "," + item).substring(1));//list(a,b,c)-->,a,b,c-->a,b,c
        String s = list.stream().reduce("", (sum, item) -> sum + "'" + item + "',");
        System.out.println("id in:" + s.substring(0, s.lastIndexOf(",")));//list(a,b,c)-->'a','b','c',-->'a','b','c'
        //all
        System.out.println("all:" + lists.stream().filter(num -> num != null).distinct().mapToInt(num -> num * 2).skip(2).limit(4).sum());
        //遍历list存入map里
        SysUserService userService = new SysUserService();
        Map<String, SysUser> collect = userService.findList().stream().collect(Collectors.toMap(SysUser::getId, Function.identity()));
    }
}
