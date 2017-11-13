package com.xwbing.demo;

import com.xwbing.entity.SysUser;
import com.xwbing.entity.SysUserRole;
import com.xwbing.service.sys.SysUserRoleService;
import com.xwbing.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Date: 2017/6/15 17:09
 * Author: xiangwb
 * Project: zdemo
 */
public class LambdaDemo {
    @Autowired
    private SysUserRoleService sysUserRoleService;

    public static void main(String[] args) {
        //匿名内部类
        Thread t = new Thread(() -> System.out.println("hello,lambda"));
        t.start();
        List<String> list = Arrays.asList("a", "b", "c");
        Collections.sort(list, (str1, str2) -> str1.compareTo(str2));

        /**
         * stream api 高级版本的迭代器
         */
        List<Integer> lists = Arrays.asList(1, 2, 4, 2, 3, 5, 5, 6, 8, 9, 7, 10);
//        //遍历
//        Arrays.stream(arrays).forEach(System.out::println);
//        list.forEach(System.out::println);
        System.out.println("sort:" + lists.stream().sorted((o1, o2) -> o2 - o1).collect(Collectors.toList()));//排序
        System.out.println("map:" + lists.stream().map(o1 -> o1 * 2).collect(Collectors.toList()));//转换
        System.out.println("distinct:" + lists.stream().distinct().collect(Collectors.toList()));//去重(去重逻辑依赖元素的equals方法)
        System.out.println("limit:" + lists.stream().limit(4).collect(Collectors.toList()));//截取
        System.out.println("skip:" + lists.stream().skip(4).collect(Collectors.toList()));//丢弃
        //过滤
        System.out.println("filter:" + lists.stream().filter(o1 -> o1 > 3 && o1 < 8).collect(Collectors.toList()));
        Predicate<Integer> gt = integer -> integer > 3;//函数式接口Predicate
        Predicate<Integer> lt = integer -> integer < 8;
        System.out.println("重用filter:" + lists.stream().filter(gt.and(lt)).collect(Collectors.toList()));
        //聚合
        System.out.println("reduce:" + lists.stream().reduce((o1, o2) -> o1 + o2).get());//聚合
        System.out.println("reduce:" + lists.stream().reduce(0, (o1, o2) -> o1 + o2));//聚合(给定默认值)
        System.out.println("ids:" + list.stream().reduce((sum, item) -> sum + "," + item).get());//常用//list(a,b,c)-->,a,b,c-->a,b,c
        System.out.println("ids:" + list.stream().reduce("", (sum, item) -> sum + "," + item).substring(1));
        String s = list.stream().reduce("", (sum, item) -> sum + "'" + item + "',");//list(a,b,c)-->'a','b','c',-->'a','b','c'
        System.out.println("id in:" + s.substring(0, s.lastIndexOf(",")));
        //join
        System.out.println("join:" + list.stream().collect(Collectors.joining(",")));//list(a,b,c)-->a,b,c
        System.out.println("join:" + String.join(",", list));
        //统计
        IntSummaryStatistics statistics = lists.stream().mapToInt(x -> x).summaryStatistics();
        System.out.println("List中最大的数字 : " + statistics.getMax());
        System.out.println("List中最小的数字 : " + statistics.getMin());
        System.out.println("所有数字的总和   : " + statistics.getSum());
        System.out.println("所有数字的平均值 : " + statistics.getAverage());
        System.out.println("List成员个数     : " + statistics.getCount());
        //all example
        System.out.println("all:" + lists.stream().filter(num -> num != null).distinct().mapToInt(num -> num * 2).skip(2).limit(4).sum());
        //遍历list存入map里
        Map<String, SysUser> collect = new SysUserService().findList().stream().collect(Collectors.toMap(SysUser::getId, Function.identity()));
        //分组
        Map<Integer, List<SysUser>> groupMap = new SysUserService().findList().stream().collect(Collectors.groupingBy(SysUser::getSex));//(分组条件为key，分组成员为value)
        //非空判断
        Optional<String> optional = list.stream().reduce((sum, item) -> sum + "," + item);
        String reduce;
        if (optional.isPresent()) {
            reduce = optional.get();
        }
        //异步回调
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();//任务线程池，在spring配置文件中配置
        poolTaskExecutor.setCorePoolSize(5);
        poolTaskExecutor.setKeepAliveSeconds(30000);
        poolTaskExecutor.setMaxPoolSize(1000);
        poolTaskExecutor.setQueueCapacity(200);

        CompletableFuture<List<SysUser>> future = CompletableFuture.supplyAsync(() -> getList(), poolTaskExecutor);
        try {
            List<SysUser> sysUsers = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * filter
     *
     * @return
     */
    public List<SysUser> getRoleUsers(int flag) {
        if (flag == 0) {//例一
            Predicate<SysUser> roles = sysUser -> {
                List<SysUserRole> sysUserRoles = sysUserRoleService.queryByUserId(sysUser.getId());
                return sysUserRoles.size() > 0;
            };
            return new ArrayList<SysUser>().stream().filter(roles).collect(Collectors.toList());
        } else {//例二
            return new ArrayList<SysUser>().stream().filter(sysUser -> {
                List<SysUserRole> sysUserRoles = sysUserRoleService.queryByUserId(sysUser.getId());
                return sysUserRoles.size() > 0;
            }).collect(Collectors.toList());
        }
    }

    public static List<SysUser> getList() {
        return null;
    }

}
