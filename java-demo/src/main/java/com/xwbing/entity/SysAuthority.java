package com.xwbing.entity;

import lombok.Data;

import java.io.Serializable;
/**
 * 说明: 权限<br/>
 * 创建日期: 2016年12月9日 下午4:10:17 <br/>
 * 作者: xwb
 */
@Data
public class SysAuthority  implements Serializable
{
	private static final long serialVersionUID = 6119897499200969917L;
	public static String table = "sys_authority";
	private String id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编号
	 */
	private String code;
	/**
	 * 是否启用
	 */
	private String isEnable;
	/**
	 * url地址
	 */
	private String url;

	/**
	 * 父ID
	 */
	private String parentId;
	private String creator;
	private String modifier;
	 /**
     * 类型  2按钮 1菜单
     */
    private int type;
}
