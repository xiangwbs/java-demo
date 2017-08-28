package com.xwbing.service.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwbing.Exception.BusinessException;
import com.xwbing.dao.SysRoleAuthorityDao;
import com.xwbing.entity.SysRoleAuthority;
import com.xwbing.util.LoginSysUserUtil;
import com.xwbing.util.RestMessage;

/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2016<br/>
 * 说明: <br/>
 * 项目名称: zdemo <br/>
 * 创建日期: 2017年1月18日 下午5:47:53 <br/>
 * 作者: xwb
 */
@Service
public class SysRoleAuthorityService {
	@Autowired
	private SysRoleAuthorityDao sysRoleAuthorityDao;
	/**
	 * 
	 * 功能描述：   执行用户角色权限保存操作,保存之前先判断是否存在，存在删除                     <br/>
	 * 作    者：xwb <br/>
	 * 创建时间：2017年1月22日  下午3:12:24 <br/>
	 * @param list
	 * @param roleId
	 * @return
	 */
	public RestMessage saveBatch(List<SysRoleAuthority> list, String roleId) {
		RestMessage result=new RestMessage();
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("role_id", roleId);
		List<SysRoleAuthority> exits=sysRoleAuthorityDao.select(params);
		if (exits != null && exits.size() > 0) {
			String id;
			Map<String, Object> map=new HashMap<String, Object>();
			for (SysRoleAuthority temp:exits){
				id=temp.getId();
				map.put("id", id);
				int row=sysRoleAuthorityDao.delete(params);
				if(row==0){
					throw new BusinessException("删除角色权限关联信息错误");
				}
			}
		}
		for(SysRoleAuthority sysRoleAuthority:list){
			sysRoleAuthority.setCreator(LoginSysUserUtil.getUserId());
			int row=sysRoleAuthorityDao.update(sysRoleAuthority);
			if(row==0){
				throw new BusinessException("批量保存数据失败");
			}
		}
		result.setMsg("批量保存成功");
		result.setSuccess(true);
		return result;
	}
	/**
	 * 
	 * 功能描述：   根据角色主键查找                     <br/>
	 * 作    者：xwb <br/>
	 * 创建时间：2017年1月22日  下午3:31:58 <br/>
	 * @param roleId
	 * @return
	 */
	public List<SysRoleAuthority> queryByRoleId(String roleId) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("role_id", roleId);
		List<SysRoleAuthority> list=sysRoleAuthorityDao.select(params);
		return list;
	}
}
