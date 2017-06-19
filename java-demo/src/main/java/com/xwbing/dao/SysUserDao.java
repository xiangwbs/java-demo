package com.xwbing.dao;


import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.xwbing.entity.SysUser;

@Repository("userDao")
public interface SysUserDao {
	public List<SysUser> select(Map<String, Object> params);
	public int count(Map<String, Object> params);
	public int delete(Map<String, Object> params);
	public int save(SysUser user);
	public int update(SysUser user);
	public SysUser findById(String id);
}
