package com.xwbing.Exception;

import org.apache.shiro.authc.AuthenticationException;


/**
 * 说明:密码失效异常
 * 创建日期: 2016年8月30日 下午8:19:49
 * 作者: xiangwb
 */
public class InvalidPassWordException extends AuthenticationException{

	private static final long serialVersionUID = -5978833014032735770L;

	public InvalidPassWordException() {

		super();

	}

	public InvalidPassWordException(String message, Throwable cause) {

		super(message, cause);

	}

	public InvalidPassWordException(String message) {

		super(message);

	}

	public InvalidPassWordException(Throwable cause) {

		super(cause);

	}
}
