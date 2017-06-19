package com.xwbing.entity;

import java.io.Serializable;

/**
 * 说明:角色 <br/>
 * 创建日期: 2016年12月9日 下午4:10:33 <br/>
 * 作者: xwb
 */
public class SysRole  implements Serializable{
	private static final long serialVersionUID = -6522237994889661036L;
	public static String table="sys_role";
	private String id;
	/**
	 * 角色名称
	 */
	private String name;
	/**
	 * 角色编码
	 */
	private String code;
	/**
	 * 是否启用
	 */
	private String isEnable;
	/**
	 * 描述
	 */
	private String remark;
	private String modifier;
	private String creator;
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	 
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public String getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	 
	
	
}
