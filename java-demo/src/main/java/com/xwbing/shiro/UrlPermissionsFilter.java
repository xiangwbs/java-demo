package com.xwbing.shiro;

import com.xwbing.entity.SysAuthority;
import com.xwbing.service.sys.SysAuthorityService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 说明: 自定义的URL认证
 * 创建日期: 2016年7月13日 上午10:01:04
 * 作者: xiangwb
 */
@Component
public class UrlPermissionsFilter extends PermissionsAuthorizationFilter {
	private String unauthorizedUrl;
	public String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}
	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}
	private SysAuthorityService sysAuthorityService;
	public void setSysAuthorityService(SysAuthorityService sysAuthorityService) {
		this.sysAuthorityService = sysAuthorityService;
	}

	/**
	 * 
	 * 是否允许访问
	 * @param mappedValue
	 * 指的是在声明url时指定的权限字符串，如 我们要动态产生这个权限字符串，
	 */
	public boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws IOException {
		mappedValue = buildPermissions(request);
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated())
			return false;
		// 主键
		if (subject.getPrincipal() == null)
			return false;
		String[] perms = (String[]) mappedValue;
		boolean isPermitted = true;
		if (perms != null && perms.length > 0) {
			if (perms.length == 1) {
				if (checkeUrlExit(perms[0])) {// 如果是需要认证的，继续，否则不继续
					if (!subject.isPermitted(perms[0])) {
						isPermitted = false;
					}
				}
			} else {
				if (!subject.isPermittedAll(perms)) {
					isPermitted = false;
				}
			}
		}
		System.out.println(isPermitted == false ? "无权限访问" : "");
		return isPermitted;
	}

	/**
	 * 判断url是否存在。
	 * 如果是返回true，表示需要认证;如果是返回false，权限列表里面没有，表示可以不需要认证
	 * @param perms
	 * @return
	 */
	private boolean checkeUrlExit(String perms) {
		// 此处是去权限URl列表里看看，如果是不需要验证的，那么直接返回true，如果需要认证的，那么继续认证
		/** start **/
		boolean exitTag = false;// 是否存在
		List<SysAuthority> list = sysAuthorityService.queryAll(null);
		if (list != null)
			for (SysAuthority sysAuthority : list) {
				String validateUrl = sysAuthority.getUrl();
				if (StringUtils.isEmpty(validateUrl))
					continue;
				if (perms.equalsIgnoreCase(validateUrl)) {
					exitTag = true;// 表示权限认证里面存在需要进行认证的
					continue;
				}
			}
		return exitTag;
		/** end **/
	}

	/**
	 * 根据请求URL产生权限字符串，这里只产生，而比对的事交给Realm
	 *
	 * @param request
	 * @return
	 */
	protected String[] buildPermissions(ServletRequest request) {
		String[] perms = new String[1];
		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getServletPath();
		perms[0] = path;// path直接作为权限字符串
		return perms;
	}

	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws IOException {
		Subject subject = getSubject(request, response);
		// If the subject isn't identified, redirect to login URL
		if (subject.getPrincipal() == null) {
			saveRequestAndRedirectToLogin(request, response);
		} else {
			// If subject is known but not authorized, redirect to the
			// unauthorized URL if there is one
			// If no unauthorized URL is specified, just return an unauthorized
			// HTTP status code
			String unauthorizedUrl = getUnauthorizedUrl();
			// SHIRO-142 - ensure that redirect _or_ error code occurs - both
			// cannot happen due to response commit:
			if (StringUtils.isNotEmpty(unauthorizedUrl)) {
				WebUtils.issueRedirect(request, response, unauthorizedUrl);
			} else {
				WebUtils.toHttp(response).sendError(
						HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		return false;
	}
}
