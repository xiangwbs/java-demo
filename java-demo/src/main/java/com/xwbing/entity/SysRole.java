package com.xwbing.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 说明:角色 <br/>
 * 创建日期: 2016年12月9日 下午4:10:33 <br/>
 * 作者: xwb
 */
@Data
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
}
