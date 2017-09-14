package com.xwbing.entity.dto;

import lombok.Data;

/**
 * 说明:
 * 项目名称: zxwbing <
 * 创建日期: 2017年3月22日 下午4:58:42
 * 作者: xiangwb
 */
@Data
public class userDto {
    //excel导出字段的顺序
    private String userName;
    private String mail;
    private String sex;
    private String isAdmin;
}
