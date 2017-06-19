package com.xwbing.entity;

import java.io.Serializable;
/**
 * 说明: 权限<br/>
 * 创建日期: 2016年12月9日 下午4:10:17 <br/>
 * 作者: xwb
 */
public class SysAuthority  implements Serializable
{
	private static final long serialVersionUID = 6119897499200969917L;
	public static String table = "sys_authority";
	private String id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编号
	 */
	private String code;
	/**
	 * 是否启用
	 */
	private String isEnable;
	/**
	 * url地址
	 */
	private String url;

	/**
	 * 父ID
	 */
	private String parentId;
	private String creator;
	private String modifier;
	 /**
     * 类型  2按钮 1菜单
     */
    private int type;
	public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}


	public String getIsEnable()
	{
		return isEnable;
	}

	public void setIsEnable(String isEnable)
	{
		this.isEnable = isEnable;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	

}
