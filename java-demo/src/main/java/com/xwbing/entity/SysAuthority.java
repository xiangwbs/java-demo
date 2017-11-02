package com.xwbing.entity;

import lombok.Data;

import java.io.Serializable;
/**
 * 说明: 权限
 * 创建日期: 2016年12月9日 下午4:10:17
 * 作者: xiangwb
 */
@Data
public class SysAuthority  implements Serializable
{
	private static final long serialVersionUID = 6119897499200969917L;
	public static String table = "sys_authority";
	private String id;
	private String name;
	private String code;
	private String isEnable;
	private String url;
	private String parentId;
	private String creator;
	private String modifier;
	 /**
     * 类型  2按钮 1菜单
     */
    private int type;
}
