package com.xwbing.validate;

import com.xwbing.util.CommonEnum;

/**
 * 
 * 说明:表单校验结果
 * 创建日期: 2016年12月19日 下午3:45:10
 * 作者: xiangwb
 */
public class ValidateResult
{
	/**
	 * 错误信息
	 */
	private String errorMsg;
	/**
	 * 错误码1通过0失败
	 */
	private int errorCode;

	public ValidateResult()
	{
		this.errorCode = CommonEnum.ValidateResultEnum.SUCCESS.getErrorCode();
	}

	public boolean isSuccess()
	{
		return CommonEnum.ValidateResultEnum.SUCCESS.getErrorCode() == this.errorCode;
	}

	public String getErrorMsg()
	{
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg)
	{
		this.errorMsg = errorMsg;
	}

	public int getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(CommonEnum.ValidateResultEnum error)
	{
		this.errorCode = error.getErrorCode();
		this.errorMsg = error.getMsg();
	}

}
