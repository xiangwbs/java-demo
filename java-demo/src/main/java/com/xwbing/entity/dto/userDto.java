package com.xwbing.entity.dto;

import lombok.Data;

/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2016<br/>
 * 说明: <br/>
 * 项目名称: zxwbing <br/>
 * 创建日期: 2017年3月22日 下午4:58:42 <br/>
 * 作者: xwb
 */
@Data
public class userDto {
    //excel导出字段的顺序
    private String userName;
    private String mail;
    private String sex;
    private String isAdmin;
}
