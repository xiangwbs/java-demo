package com.xwbing.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 说明:
 * 项目名称: zxwbing
 * 创建日期: 2017年4月13日 下午3:49:30
 * 作者: xiangwb
 */
@Component
public class MyTask {
    @Scheduled(cron = "0 */5 * * * ?")//每5分钟执行一次 
    public void doTask(){
        
    }
}
