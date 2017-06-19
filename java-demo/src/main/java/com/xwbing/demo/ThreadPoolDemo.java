package com.xwbing.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2016<br/>
 * 说明: <br/>
 * 项目名称: zdemo <br/>
 * 创建日期: 2017年2月20日 下午5:44:06 <br/>
 * 作者: xwb
 */

public class ThreadPoolDemo {
    private static int produceTaskSleepTime = 2;
    private static int produceTaskMaxNumber = 10;
    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        
    }
}
