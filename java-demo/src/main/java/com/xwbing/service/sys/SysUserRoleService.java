package com.xwbing.service.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xwbing.Exception.BusinessException;
import com.xwbing.dao.SysUserRoleDao;
import com.xwbing.entity.SysUserRole;
import com.xwbing.util.LoginSysUserUtil;
import com.xwbing.util.RestMessage;

/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2016<br/>
 * 说明: <br/>
 * 项目名称: zdemo <br/>
 * 创建日期: 2017年1月18日 下午5:48:10 <br/>
 * 作者: xwb
 */
@Service
public class SysUserRoleService {
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	public RestMessage deleteById(String id){
		RestMessage result=new RestMessage();
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("id", id);
		int row=sysUserRoleDao.delete(params);
		if (row == 1) {
			result.setMsg("删除用户角色关联成功");
			result.setSuccess(true);
		} else {
			result.setMsg("删除用户角色关联失败");
		}
		return result;
		
		
	}

	/**
	 * 
	 * 功能描述：执行用户角色权限保存操作,保存之前先判断是否存在，存在删除 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月22日 下午2:32:11 <br/>
	 * 
	 * @param list
	 * @param userId
	 * @return
	 */
	public RestMessage saveBatch(List<SysUserRole> list, String userId) {
		RestMessage result=new RestMessage();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", userId);
		List<SysUserRole> exits=sysUserRoleDao.select(params);
		if (exits != null && exits.size() > 0) {
			Map<String, Object> map=new HashMap<String, Object>();
			String id;
			for (SysUserRole temp : exits){
				id=temp.getId();
				map.put("id", id);
				int row=sysUserRoleDao.delete(params);
				if(row==0){
					throw new BusinessException("删除用户角色关联信息错误");
				}
			}
		}
		for(SysUserRole sysUserRole:list){
			sysUserRole.setCreator(LoginSysUserUtil.getUserId());
			int row=sysUserRoleDao.update(sysUserRole);
			if(row==0){
				throw new BusinessException("删除用户角色关联信息错误");
			}
		}
		result.setMsg("批量保存用户角色关联成功");
		result.setSuccess(true);
		return result;
	}
	/**
	 * 
	 * 功能描述：    根据用户主键获取                    <br/>
	 * 作    者：xwb <br/>
	 * 创建时间：2017年1月22日  下午3:05:31 <br/>
	 * @param userId
	 * @return
	 */
	public List<SysUserRole> queryByUserId(String userId) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("user_id", userId);
		List<SysUserRole> list=sysUserRoleDao.select(params);
		return list;
	}
	/**
	 * 
	 * 功能描述：      分页查询                  <br/>
	 * 作    者：xwb <br/>
	 * 创建时间：2017年1月22日  下午4:14:49 <br/>
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<SysUserRole> queryPage(int pageNo ,int pageSize){
		if(pageNo<1){
			pageNo=1;
		}
		if(pageSize<1){
			pageSize=20;
		}
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("firstResult", (pageNo - 1) * pageSize);
		params.put("offset", pageSize);
		List<SysUserRole> list=sysUserRoleDao.select(params);
		return list;
	}
	
	
}
