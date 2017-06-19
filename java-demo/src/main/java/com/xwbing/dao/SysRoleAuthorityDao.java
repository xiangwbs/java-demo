package com.xwbing.dao;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.xwbing.entity.SysRoleAuthority;

/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2016<br/>
 * 说明: <br/>
 * 项目名称: zdemo <br/>
 * 创建日期: 2017年1月20日 下午5:22:37 <br/>
 * 作者: xwb
 */
@Repository("sysRoleAuthorityDao")
public interface SysRoleAuthorityDao {
	public List<SysRoleAuthority> select(Map<String, Object> params);
	public int count(Map<String, Object> params);
	public int delete(Map<String, Object> params);
	public int save(SysRoleAuthority roleAuthority);
	public int update(SysRoleAuthority roleAuthority);
}
