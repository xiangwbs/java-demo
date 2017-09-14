package com.xwbing.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import com.xwbing.entity.SysUser;

/**
 * 
 * 说明:获取登录用戶信息
 * 创建日期: 2016年11月29日 上午10:28:50
 * 作者: xiangwb
 */
public class LoginSysUserUtil {

	public static String getUserId() {
		SysUser sysUser = getSysUser();
		return sysUser.getId();
	}

	public static SysUser getSysUser() {
		Subject subject = SecurityUtils.getSubject();
		Object object = subject.getPrincipal();
		SysUser sysUser = null;
		if (object != null) {
			sysUser = (SysUser) object;
		} else{
			sysUser =new SysUser();
			sysUser.setId("develop");
		}
		return sysUser;
	}
}
