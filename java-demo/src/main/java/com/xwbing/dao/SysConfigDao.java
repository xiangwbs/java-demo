package com.xwbing.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xwbing.entity.SysConfig;

/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2016<br/>
 * 说明: <br/>
 * 项目名称: zxwbing <br/>
 * 创建日期: 2017年3月7日 下午1:46:39 <br/>
 * 作者: xwb
 */
@Repository("sysConfigDao")
public interface SysConfigDao {
    public int save(SysConfig sysConfig);
    public int delete(String id);
    public int update(SysConfig sysConfig);
    public SysConfig findOne(Map<String, Object> params);
}
