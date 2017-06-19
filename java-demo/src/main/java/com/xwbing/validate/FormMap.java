package com.xwbing.validate;

import java.util.HashMap;
/**
 * 
 * 说明: 前台提交表单校验配置 的map<br/>
 * 创建日期: 2016年12月19日 下午3:49:24 <br/>
 * 作者: xwb
 */
public class FormMap {

	/**
	 * 用户新增接口校验规则配置
	 */
	private static HashMap<String, String> sysUserAddMap = new HashMap<String, String>();

	/**
	 * 用户保存接口校验规则配置
	 */
	private static HashMap<String, String> sysUserUpdateMap = new HashMap<String, String>();
	/**
	 * 角色新增保存接口校验规则配置
	 */
	private static HashMap<String, String> sysRoleMap = new HashMap<String, String>();

	/**
	 * 权限新增保存接口校验规则配置
	 */
	private static HashMap<String, String> sysAuthorityMap = new HashMap<String, String>();

	public static HashMap<String, String> getSysUserAddMap() {
		return sysUserAddMap;
	}

	public static void setSysUserAddMap(HashMap<String, String> sysUserAddMap) {
		FormMap.sysUserAddMap = sysUserAddMap;
	}

	public static HashMap<String, String> getSysUserUpdateMap() {
		return sysUserUpdateMap;
	}

	public static void setSysUserUpdateMap(HashMap<String, String> sysUserUpdateMap) {
		FormMap.sysUserUpdateMap = sysUserUpdateMap;
	}

	public static HashMap<String, String> getSysRoleMap() {
		return sysRoleMap;
	}

	public static void setSysRoleMap(HashMap<String, String> sysRoleMap) {
		FormMap.sysRoleMap = sysRoleMap;
	}

	public static HashMap<String, String> getSysAuthorityMap() {
		return sysAuthorityMap;
	}

	public static void setSysAuthorityMap(HashMap<String, String> sysAuthorityMap) {
		FormMap.sysAuthorityMap = sysAuthorityMap;
	}

	
}