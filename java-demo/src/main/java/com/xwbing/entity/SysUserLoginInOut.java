package com.xwbing.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 说明: 用户登录登出表<br/>
 * 创建日期: 2016年11月2日 下午3:15:27 <br/>
 * 作者: xwb
 */
public class SysUserLoginInOut implements Serializable{
	private static final long serialVersionUID = -4900634293315431656L;
	public static String table="sys_user_login_in_out";
	private String id;
	/**
	 * ip
	 */
	private String ip;
	
	/**
	 * 记录时间
	 */
	private Date recordDate;	
	/**
	 * 开始时间 临时字段
     */
	@JSONField(serialize=false)
	private String startDate;
	/**
	 * 结束时间 临时字段
	 */
	@JSONField(serialize=false)
	private String endDate;
	
	
	/**
	 * 用户主键	
	 */
	private String userId;	
	
	/**
	 * 用户名称 临时字段
	 */
	@JSONField(serialize =false)
	private String userIdName;	
	
	/**
	 * 登录还是登出 1登录，0登出
	 */
	
	private int inoutType;
	/**
	 * 临时字段 
	 */
	@JSONField(serialize =false)
	private String inoutTypeName;	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getUserIdName() {
		return userIdName;
	}
	public void setUserIdName(String userIdName) {
		this.userIdName = userIdName;
	}
	public String getInoutTypeName() {
		return inoutTypeName;
	}
	public void setInoutTypeName(String inoutTypeName) {
		this.inoutTypeName = inoutTypeName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getInoutType() {
		return inoutType;
	}
	public void setInoutType(int inoutType) {
		this.inoutType = inoutType;
	}	
	
	
	
}
