package com.xwbing.entity.vo;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.xwbing.entity.SysRole;
/**
 * 说明: RoleVo<br/>
 * 项目名称: zdemo <br/>
 * 创建日期: 2016年12月9日 下午3:29:06 <br/>
 * 作者: xwb
 */

public class RoleVo {
	public RoleVo() {
	}
	public RoleVo(SysRole role) {
		try {
			BeanUtils.copyProperties(this, role);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private List<RoleVo> children;
	public List<RoleVo> getChildren() {
		return children;
	}
	public void setChildren(List<RoleVo> children) {
		this.children = children;
	}
}
