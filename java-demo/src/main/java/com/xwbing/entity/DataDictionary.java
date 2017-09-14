package com.xwbing.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 
 * 说明:数据字典
 * 创建日期: 2016年11月2日 下午3:14:37
 * 作者: xiangwb
 */
@Data
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
}
