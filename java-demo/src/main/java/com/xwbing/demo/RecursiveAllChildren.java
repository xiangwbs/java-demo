package com.xwbing.demo;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.xwbing.entity.SysRole;
import com.xwbing.entity.vo.RoleVo;

/**
 * 说明: 递归demo
 * 项目名称: zdemo
 * 创建日期: 2016年12月9日 下午3:27:47
 * 作者: xiangwb
 */

public class RecursiveAllChildren {
	/**
	 * 递归查询
	 * 
	 * @param upLink
	 * @return
	 */
	public List<RoleVo> queryAllChildren(String root)
	{
		// 获取结果
		List<SysRole> roles = queryByUplink(root);
		if (CollectionUtils.isEmpty(roles))
			return null;
		List<RoleVo> result = new ArrayList<RoleVo>();
		for (SysRole role : roles)
		{
			RoleVo vo=new RoleVo(role);
			vo.setChildren(queryAllChildren(role.getId()));
			result.add(vo);
		}
		return result;
	}
	/***
	 * 假设据父ID进行查询
	 */
	public List<SysRole> queryByUplink(String root){
		return new ArrayList<SysRole>();
	}
}
