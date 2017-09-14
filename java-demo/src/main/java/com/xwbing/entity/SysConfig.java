package com.xwbing.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 说明: 系统配置
 * 创建日期: 2016年9月27日 下午1:15:47
 * 作者: xiangwb
 */
@Data
public class SysConfig extends BaseEntity {
	private static final long serialVersionUID = -3683714522374175290L;
	public static String table = "system_config";
	/**
	 * 配置项的key
	 */
	private String code;
	/**
	 * 配置项的值
	 */
	private String value;
	/**
	 * 是否启用
	 */
	@JSONField(name = "is_enable")
	private String isEnable;
	/**
	 * 配置项的描述（名称）
	 */
	private String name;
}
