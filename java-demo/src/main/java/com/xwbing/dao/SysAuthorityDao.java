package com.xwbing.dao;


import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.xwbing.entity.SysAuthority;

@Repository("authorityDao")
public interface SysAuthorityDao {
	public List<SysAuthority> select(Map<String, Object> params);
	public int count(Map<String, Object> params);
	public int delete(Map<String, Object> params);
	public int save(SysAuthority authority);
	public int update(SysAuthority authority);
	public SysAuthority findById(String id);
	public List<SysAuthority> findByAuthIds(List<String> authIds);
}
