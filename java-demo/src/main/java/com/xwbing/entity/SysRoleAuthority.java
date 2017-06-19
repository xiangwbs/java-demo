package com.xwbing.entity;

import java.io.Serializable;

/**
 * 说明: 角色权限关系表<br/>
 * 创建日期: 2016年12月9日 下午4:11:02 <br/>
 * 作者: xwb
 */
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
	
	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(String authorityId) {
		this.authorityId = authorityId;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
