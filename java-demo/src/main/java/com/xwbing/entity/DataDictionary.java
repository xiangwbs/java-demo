package com.xwbing.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2016<br/>
 * 说明:数据字典 <br/>
 * 项目名称: cloud-dataCenter-entity <br/>
 * 创建日期: 2016年11月2日 下午3:14:37 <br/>
 * 作者: xwb
 */
class DataDictionary extends BaseEntity {
    private static final long serialVersionUID = 1699448242403042453L;
    public static String table="data_dictionary";
	/**
	 * 英文名称  编码 唯一
	 */
	private String code;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 字典值
	 */
	private String value;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 父键
	 */
	@JSONField(name="parent_id")
	private String parentId;
	
	/**
	 * 是否启用
	 */
	@JSONField(name="is_enable")
	private String isEnable;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	 

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getParentId() {
		return parentId;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getIsEnable() {
		return isEnable;
	}
	
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	
}
