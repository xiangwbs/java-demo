package com.xwbing.service.sys;

import com.xwbing.Exception.BusinessException;
import com.xwbing.dao.SysRoleDao;
import com.xwbing.entity.SysRole;
import com.xwbing.entity.SysUserRole;
import com.xwbing.util.LoginSysUserUtil;
import com.xwbing.util.RestMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明:
 * 项目名称: zdemo
 * 创建日期: 2017年1月18日 下午5:46:46
 * 作者: xiangwb
 */
@Service
public class SysRoleService {
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 
	 * 功能描述： 根据主键查询 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月22日 上午10:12:40 <br/>
	 * 
	 * @param id
	 * @return
	 */
	public SysRole findById(String id) {
		SysRole role = sysRoleDao.findById(id);
		return role;
	}

	/**
	 * 
	 * 功能描述： 保存 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月22日 上午10:13:09 <br/>
	 * 
	 * @param role
	 * @return
	 */
	public RestMessage save(SysRole role) {
		RestMessage result = new RestMessage();
		int row = sysRoleDao.save(role);
		if (row == 1) {
			result.setMsg("保存角色成功");
			result.setSuccess(true);
		} else {
			result.setMsg("保存角色失败");
		}
		return result;
	}

	/**
	 * 
	 * 功能描述： 更新 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月22日 上午10:13:16 <br/>
	 * 
	 * @param role
	 * @return
	 */
	public RestMessage update(SysRole role) {
		RestMessage result = new RestMessage();
		SysRole old = sysRoleDao.findById(role.getId());
		if (old == null) {
			throw new BusinessException("该角色不存在");
		}
		old.setName(role.getName());
		old.setCode(role.getCode());
		old.setIsEnable(role.getIsEnable());
		old.setRemark(role.getRemark());
		old.setModifier(LoginSysUserUtil.getUserId());
		int row = sysRoleDao.update(old);
		if (row == 1) {
			result.setMsg("更新角色成功");
			result.setSuccess(true);
		} else {
			result.setMsg("更新角色失败");
		}
		return result;

	}

	/**
	 * 
	 * 功能描述： 列表查询 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月22日 上午10:13:28 <br/>
	 * 
	 * @param isEnable
	 * @return
	 */
	public List<SysRole> queryAll(String isEnable) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(isEnable)) {
			params.put("is_enable", isEnable);
		}
		List<SysRole> list = sysRoleDao.select(params);
		return list;

	}

	public List<SysRole> queryByUserId(String userId, String isEnable) {
		List<SysUserRole> list = sysUserRoleService.queryByUserId(userId);
		if (list.size() == 0)
			return new ArrayList<SysRole>();
		List<String> roleIds=new ArrayList<String>();
		for (SysUserRole re : list) {
			roleIds.add(re.getRoleId());
		}
		List<SysRole> lists=sysRoleDao.findByRoleIds(roleIds);
		return lists;
		

	}

	/**
	 * 
	 * 功能描述： 检验编码唯一性 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月22日 上午10:13:54 <br/>
	 * 
	 * @param code
	 * @param id
	 * @return
	 */
	public boolean uniqueCode(String code, String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		if (StringUtils.isNotEmpty(id)) {// 如果不为空，那么不查询自己的
			params.put("id", id);
		}
		List<SysRole> list = sysRoleDao.select(params);
		if (list != null && list.size() > 0)
			return false;
		else
			return true;
	}
	public RestMessage deleteById(String id){
	    return null;
	}
	
	public List<SysRole> findShowListByPage(SysRole role) {
	    Map<String, Object> params = new HashMap<String, Object>();
	    if (null != role) {
	        if (StringUtils.isNotEmpty(role.getName())) {
	            params.put("name", role.getName());
	        }
	        if (StringUtils.isNotEmpty(role.getIsEnable())) {
	            params.put("is_enable", role.getIsEnable());
	        }
	    }
	    List<SysRole> list=sysRoleDao.select(params);
	    return list;
	    
	}
}
