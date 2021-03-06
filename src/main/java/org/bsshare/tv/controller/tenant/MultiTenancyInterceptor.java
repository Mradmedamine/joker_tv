package org.bsshare.tv.controller.tenant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bsshare.tv.configuration.multitenancy.TenantContext;
import org.bsshare.tv.model.front.web.Tenant;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class MultiTenancyInterceptor extends HandlerInterceptorAdapter {

	private static final String API_URI_HEAD = "/unsecured/api/";

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		String uri = req.getRequestURI().trim();
		if (uri.startsWith(API_URI_HEAD)) {
			Tenant tenant = parseTenantString(extractTenantName(uri));
			if (tenant == null) {
				TenantContext.setDefaultTenant();
			} else {
				TenantContext.setCurrentTenant(tenant.getId());
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		TenantContext.clear();
	}

	private String extractTenantName(String uri) {
		String tenantStr = null;
		try {
			tenantStr = uri.substring(indexOfLastCharacter(uri, API_URI_HEAD), uri.indexOf("/", indexOfLastCharacter(uri, API_URI_HEAD)));
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
		return tenantStr;
	}

	private int indexOfLastCharacter(String target, String searchedString) {
		return target.indexOf(searchedString) + searchedString.length();
	}

	private Tenant parseTenantString(String tenantStr) {
		try {
			return Tenant.valueOf(tenantStr.toUpperCase());
		} catch (IllegalArgumentException | NullPointerException ex) {
			// do nothing
		}
		return null;
	}
}
