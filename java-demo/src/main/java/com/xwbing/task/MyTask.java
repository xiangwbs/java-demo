package com.xwbing.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2016<br/>
 * 说明: <br/>
 * 项目名称: zxwbing <br/>
 * 创建日期: 2017年4月13日 下午3:49:30 <br/>
 * 作者: xwb
 */
@Component
public class MyTask {
    @Scheduled(cron = "0 */5 * * * ?")//每5分钟执行一次 
    public void doTask(){
        
    }
}
