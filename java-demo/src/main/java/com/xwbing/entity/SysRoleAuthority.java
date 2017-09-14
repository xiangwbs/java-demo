package com.xwbing.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 说明: 角色权限关系表
 * 创建日期: 2016年12月9日 下午4:11:02
 * 作者: xiangwb
 */
@Data
public class SysRoleAuthority implements Serializable {
	private static final long serialVersionUID = 4586203827177099455L;
	public static String table = "sys_role_authority";
	private String id;
	/**
	 * 角色主键
	 */
	private String roleId;
	/**
	 * 权限主键
	 */
	private String authorityId;
	private String modifier;
	private String creator;
}
