package com.xwbing.service.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.xwbing.Exception.BusinessException;
import com.xwbing.dao.SysAuthorityDao;
import com.xwbing.entity.SysAuthority;
import com.xwbing.entity.SysRoleAuthority;
import com.xwbing.entity.vo.AuthVo;
import com.xwbing.util.CommonConstant;
import com.xwbing.util.LoginSysUserUtil;
import com.xwbing.util.restMessage;

/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2016<br/>
 * 说明: <br/>
 * 项目名称: zdemo <br/>
 * 创建日期: 2017年1月18日 下午5:45:44 <br/>
 * 作者: xwb
 */
@Service
public class SysAuthorityService {
	@Autowired
	private SysAuthorityDao sysAuthorityDao;
	@Autowired
	private SysRoleAuthorityService sysRoleAuthorityService;
	public boolean uniqueCode(String code, String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		if (StringUtils.isNotEmpty(id)) {// 如果不为空，那么不查询自己的
			params.put("id", id);
		}
		List<SysAuthority> list = sysAuthorityDao.select(params);
		if (list != null && list.size() > 0)
			return false;
		else
			return true;
	}

	public List<SysAuthority> queryList(String isEnable) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(isEnable)) {// 如果不为空，那么不查询自己的
			params.put("is_enable", isEnable);
		}
		List<SysAuthority> list = sysAuthorityDao.select(params);
		return list;
	}

	public SysAuthority findById(String id) {
		return sysAuthorityDao.findById(id);
	}

	public restMessage save(SysAuthority authority) {
		restMessage result = new restMessage();
		if (!uniqueCode(authority.getCode(), null)) {
			throw new BusinessException("编号不能使用");
		}
		if (StringUtils.isEmpty(authority.getParentId())) {
			authority.setParentId(CommonConstant.ROOT);
		}
		authority.setCreator(LoginSysUserUtil.getUserId());
		int row = sysAuthorityDao.save(authority);
		if (row == 1) {
			result.setMsg("保存权限成功");
			result.setSuccess(true);
		} else {
			result.setMsg("保存权限失败");
		}
		return result;
	}
	/**
     * 更新
     * 
     * @param sysAuthority
     * @return
     */
    public restMessage update(SysAuthority sysAuthority) {
        restMessage result = new restMessage();
        if (!uniqueCode(sysAuthority.getCode(), sysAuthority.getId())) {
            result.setMsg("编号不能使用");
            return result;
        }
        SysAuthority old = findById(sysAuthority.getId());
        old.setName(sysAuthority.getName());
        old.setCode(sysAuthority.getCode());
        old.setIsEnable(sysAuthority.getIsEnable());
        old.setType(sysAuthority.getType());
        old.setUrl(sysAuthority.getUrl());
        old.setModifier(LoginSysUserUtil.getUserId());
        int row=sysAuthorityDao.update(old);
        if (row == 1) {
            result.setMsg("更新权限成功");
            result.setSuccess(true);
        } else {
            result.setMsg("更新权限失败");
        }
        return result;
    }

	/**
	 * 
	 * 功能描述： 查询所有子节点 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月22日 上午10:41:50 <br/>
	 * 
	 * @param parentId
	 * @param enable
	 * @return
	 */
	public List<SysAuthority> queryByParentId(String parentId, String isEnable) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isEmpty(parentId)) {
			parentId = CommonConstant.ROOT;
		}
		params.put("parent_id", parentId);
		if (StringUtils.isNotEmpty(isEnable)) {
			params.put("is_enable", isEnable);
		}
		List<SysAuthority> list = sysAuthorityDao.select(params);
		return list;
	}

	public List<SysAuthority> queryByRoleId(String roleId, String enable) {
		List<SysRoleAuthority> list=sysRoleAuthorityService.queryByRoleId(roleId);
		if(list.size()== 0)
			return new ArrayList<SysAuthority>();
		List<String> authIds=new ArrayList<String>();
		for(SysRoleAuthority re:list){
			authIds.add(re.getAuthorityId());
		}
		List<SysAuthority> lists=sysAuthorityDao.findByAuthIds(authIds);
		return lists;
	}

	/**
	 * 
	 * 功能描述： 递归查询父节点下所有权限的id集合,并将状态设置为禁用 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月22日 上午11:02:31 <br/>
	 * 
	 * @param parentId
	 * @param list
	 * @return
	 */
	public List<JSONObject> queryAllChildren(String parentId) {
		List<SysAuthority> authoritys = queryByParentId(parentId,
				CommonConstant.ISENABLE);
		List<JSONObject> list = new ArrayList<JSONObject>();
		String id;
		JSONObject r;
		for (SysAuthority SysAuthority : authoritys) {
			id = SysAuthority.getId();
			r = new JSONObject();
			r.put("pkid", id);
			r.put("is_enable", CommonConstant.ISNOTENABLE);
			list.add(r);
			list.addAll(queryAllChildren(id));
		}
		return list;
	}

	/**
	 * 
	 * 功能描述： 根据父节点禁用所有子节点 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月22日 下午1:39:10 <br/>
	 * 
	 * @param parentId
	 * @return
	 */////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean disableChildrenByParentId(String parentId) {
		List<JSONObject> queryAllChildren = queryAllChildren(parentId);
		if (CollectionUtils.isNotEmpty(queryAllChildren)) {
			for (JSONObject jsonObject : queryAllChildren) {
				jsonObject.put("creator", LoginSysUserUtil.getUserId());
			}
			restMessage updateBatch = updateBatchObject(SysAuthority.table,
					queryAllChildren);
			return updateBatch.isSuccess();
		}
		return false;
	}

	private restMessage updateBatchObject(String table,
                                          List<JSONObject> queryAllChildren) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
	 * 
	 * 功能描述： 获取所有权限 根据状态 <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月22日 下午1:59:07 <br/>
	 * 
	 * @param isEnable
	 * @return
	 */
	public List<SysAuthority> queryAll(String isEnable) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(isEnable)) {
			params.put("is_enable", isEnable);
		}
		// 获取结果
		List<SysAuthority> queryList = sysAuthorityDao.select(params);
		return queryList;
	}

	/**
	 * 
	 * 功能描述： 递归查询所有节点（包括禁用） <br/>
	 * 作 者：xwb <br/>
	 * 创建时间：2017年1月22日 下午2:01:49 <br/>
	 * 
	 * @param parentId
	 * @param datas
	 * @param enable
	 * @return
	 */

	public List<AuthVo> queryAllChildren(String parentId,String isEnable) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parent_id", parentId);
		if (StringUtils.isNotEmpty(isEnable)) {
			params.put("is_enable", isEnable);
		}
		// 获取结果
		List<SysAuthority> queryList = sysAuthorityDao.select(params);
		if (queryList == null || queryList.size() == 0)
			return null;
		List<AuthVo> datas=new ArrayList<AuthVo>();
		AuthVo temp;
		for (int i = 0; i < queryList.size(); i++) {
			temp = new AuthVo(queryList.get(i));
			temp.setChildren(queryAllChildren(temp.getId(), isEnable));
			datas.add(temp);
		}
		return datas;
	}
	public restMessage deleteById(String id){
        return null;
    }
}
