package com.xwbing.demo;

import com.xwbing.util.DateUtil;

import java.util.Date;

/**
 * 说明:
 * 项目名称: zdemo
 * 创建日期: 2017年2月20日 上午11:32:55
 * 作者: xwb
 */

public class ThreadDemo {
    public static void main(String[] args) {
        /**
         * 直接继承thread并重写run方法，在其中定义当前线程要执行的任务 
         * 缺点：
         * 1：由于要求继承thread类，那么在将来项目中，就有可能出现继承冲突，
         * 很多时候我们需要继承项目中的一个父类来复用方法，但是当前类又要继承thread作为线程去使用 java是单继承的，这里产生类冲突
         * 2：由于继承了thread后需要重写run方法来定义线程要执行的任务，这就导致线程与任务有一个强耦合关系
         * 当前线程就只能做run方法定义的任务，其他事情做不了，这不利于线程的重用
         */
        Thread t1 = new MyThread();
        t1.start();// 启动线程要调用start方法

        /**
         * 定义一个类并实现runable接口来单独定义线程要执行的任务
         */
        Runnable r1 = new MyRunnable();
        Thread t2 = new Thread(r1);
        t2.start();

        /**
         * 使用匿名内部类实现两种创建线程的方式
         */
        // 方式一：
        Thread thread1 = new Thread() {
            public void run() {
                // TUDO
            }
        };
        // 方式二：
        Runnable runnable = new Runnable() {
            public void run() {
                Thread.yield();////模拟cpu时间片耗尽，线程发生切换
            }
        };
        Thread thread2 = new Thread(runnable);
        // 方式三：匿名对象方式(推荐)
        Thread thread3 = new Thread(new Runnable() {// 使用sleep阻塞，实现电子表功能
                    public void run() {
                        while (true) {
                            System.out.println(DateUtil.date2Str(new Date(),DateUtil.YYYY_MM_DD_HH_MM_SS));
                            try {
                                Thread.sleep(1000);// 线程进入n毫秒阻塞状态
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        /**
         * 获取线程相关信息的api
         */
        Thread main = Thread.currentThread();// 获取的就是运行该方法的线程
        long id = main.getId();// 线程id
        String name = main.getName();// 获取名字
        int priority = main.getPriority();// 查看优先级
        boolean isAlive = main.isAlive();// 是否还活着
        boolean isDaemon = main.isDaemon();// 是否为守护线程
        /**
         * 设置线程优先级： 1最低，5为默认，10最高，
         */
        thread1.setPriority(10);
        thread3.setPriority(1);
        thread1.start();
        thread2.start();
        thread3.start();

        /**
         * void setDaemon(boolean on) 
         * 后台线程，又叫做守护线程 当一个进程中的所有前台线程都结束了，进程就会结束
         * 无论进程中的其他后台线程是否还在运行，都要被强制中断。
         */
        Thread rose = new Thread() {// 前台线程
            public void run() {
            }
        };
        Thread jack = new Thread() {// 后台线程
            public void run() {
            }
        };
        // 设置为后台线程，需要在start前调用
        jack.setDaemon(true);
        rose.start();
        jack.start();
    }
}

/**
 * void join（）
 * 线程提供了一个方法join，该方法允许一个线程在另一个线程上等待， 直到它的工作结束，才继续当前线程的后续工作
 * 否则一致处于阻塞状态
 */
class Join {
    public static void main(String[] args) {
        final Thread t1 = new Thread() {
            public void run() {
              //一些耗时的操作
            }
        };
        Thread t2 = new Thread() {
            public void run() {
                try {
                    t1.join();//这里t2线程会开始阻塞，直到t1线程的run方法执行完毕
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //以下是当前线程的任务代码,只有t1线程运行完毕才会运行。
            }
        };
        t1.start();
        t2.start();
    }
}

class MyThread extends Thread {
    public void run() {// run方法是线程要执行的任务
        // TUDO
    }
}

class MyRunnable implements Runnable {
    public void run() {
        // TUDO
    }
}