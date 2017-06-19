package com.xwbing.dao;


import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.xwbing.entity.SysRole;

@Repository("roleDao")
public interface SysRoleDao {
	public List<SysRole> select(Map<String, Object> params);
	public int count(Map<String, Object> params);
	public int delete(Map<String, Object> params);
	public int save(SysRole role);
	public int update(SysRole role);
	public SysRole findById(String id);
	public List<SysRole> findByRoleIds(List<String> roleIds);
}
