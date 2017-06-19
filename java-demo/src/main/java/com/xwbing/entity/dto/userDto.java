package com.xwbing.entity.dto;
/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2016<br/>
 * 说明: <br/>
 * 项目名称: zxwbing <br/>
 * 创建日期: 2017年3月22日 下午4:58:42 <br/>
 * 作者: xwb
 */

public class userDto {
    //excel导出字段的顺序
    private String userName;
    private String mail;
    private String sex;
    private String isAdmin;
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
  
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getIsAdmin() {
        return isAdmin;
    }
    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
    
}
