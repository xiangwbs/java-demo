package com.xwbing.shiro;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


/**
 * 说明: session超时过滤 <br/>
 * 创建日期: 2016年12月8日 上午9:33:16 <br/>
 * 作者: xwb
 */
public class SessionFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		// 只过滤了ajax请求时session超时
		if (httpServletRequest.getHeader("x-requested-with") != null
				&& httpServletRequest.getHeader("x-requested-with")
						.equalsIgnoreCase("XMLHttpRequest")) {
			Subject subject = SecurityUtils.getSubject();
			if (subject.getPrincipals() == null ||  subject.getPrincipals()
					.getPrimaryPrincipal()==null ) {
				// 如果是ajax请求响应头会有，x-requested-with
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				PrintWriter out = null;
				try {
					out = response.getWriter();
					out.append("登录超时，请重新登录!");
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						out.close();
					}
				}
				return;
			}
		}

		chain.doFilter(request, response);

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}
