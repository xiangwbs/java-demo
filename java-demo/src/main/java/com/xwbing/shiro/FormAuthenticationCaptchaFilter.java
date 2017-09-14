package com.xwbing.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 说明: 基于表单认证的过滤器
 * 创建日期: 2016年8月29日 上午11:04:54
 * 作者: xiangwb
 */
public class FormAuthenticationCaptchaFilter extends FormAuthenticationFilter {

	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;

	public String getCaptchaParam() {

		return captchaParam;

	}

	protected String getCaptcha(ServletRequest request) {

		return WebUtils.getCleanParam(request, getCaptchaParam());

	}
    @Override
	protected AuthenticationToken createToken(

	ServletRequest request, ServletResponse response) {

		String username = getUsername(request);

		String password = getPassword(request);

		String captcha = getCaptcha(request);

		boolean rememberMe = isRememberMe(request);

		String host = getHost(request);

		return new UsernamePasswordCaptchaToken(username,
				password.toCharArray(), rememberMe, host, captcha);

	}

}