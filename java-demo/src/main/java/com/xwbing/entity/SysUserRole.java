package com.xwbing.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 说明:用户角色表 <br/>
 * 创建日期: 2016年12月9日 下午4:11:14 <br/>
 * 作者: xwb
 */
@Data
public class SysUserRole implements Serializable {
	private static final long serialVersionUID = -6992258272354335712L;
	public static String table="sys_user_role";
	private String id;
    /**
     * 用户主键
     */
	private String userId;
    /**
     * 角色主键
     */
	private String roleId;
	private String modifier;
	private String creator;
}
