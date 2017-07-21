package com.xwbing.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称: java-demo
 * 创建时间: 2017/6/19 17:11
 * 作者: xiangwb
 * 说明: 基础实体类
 */
@Data
public class BaseEntity  implements Serializable{
	private static final long serialVersionUID = -1836425279862671853L;
	private String id;
	@JSONField(name="sort")
	private int sort;
	@JSONField(name="synch_status")
	private String synchStatus;
	private String creator;
	private String modifier;
	@JSONField(name="is_deleted")
	private String isDeleted;
	@JSONField(name="create_time")
	private Date createTime;
	@JSONField(name="modified_time")
	private Date modifiedTime;
	@JSONField(name="datafrom")
	private String datafrom;
}
