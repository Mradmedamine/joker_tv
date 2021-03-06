package org.bsshare.tv.configuration.multitenancy;

import java.util.Optional;

import org.bsshare.tv.model.front.web.Tenant;
import org.bsshare.tv.model.front.web.UserTenantDetails;
import org.bsshare.tv.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class TenantContext {

	private static Logger logger = LoggerFactory.getLogger(TenantContext.class.getName());

	private static final int SCOPE_REQUEST = RequestAttributes.SCOPE_REQUEST;
	private static final String TENANT_ID_KEY = "tenantId";

	public static final String ANONYMOUS_TENANT_ID = Tenant.ANONYMOUS.getId();
	public static final String DEFAULT_TENANT_ID = Tenant.JOKER.getId();

	public static void setCurrentTenant(String tenantId) {
		logger.debug("Setting tenant to " + tenantId);
		Optional<Tenant> tenant = Tenant.fromId(tenantId);
		Optional<UserTenantDetails> userTenantDetails = SecurityService.getUserTenantDetails();
		if (userTenantDetails.isPresent()) {
			userTenantDetails.get().setTenant(tenant.orElse(Tenant.ANONYMOUS));
		} else {
			getRequestAttributes().ifPresent(attributes -> {
				attributes.setAttribute(TENANT_ID_KEY, tenantId, SCOPE_REQUEST);
			});
		}
	}

	public static void setDefaultTenant() {
		setCurrentTenant(DEFAULT_TENANT_ID);
	}

	public static String getCurrentTenant() {
		Optional<UserTenantDetails> userTenantDetails = SecurityService.getUserTenantDetails();
		if (userTenantDetails.isPresent()) {
			return userTenantDetails.get().getTenant().getId();
		} else {
			String tenantId = getRequestAttributes()
					.map(attributes -> attributes.getAttribute(TENANT_ID_KEY, SCOPE_REQUEST))
					.map(String.class::cast)
					.orElse(null);
			return tenantId != null ? tenantId : ANONYMOUS_TENANT_ID;
		}
	}

	private static Optional<RequestAttributes> getRequestAttributes() {
		return Optional.ofNullable(RequestContextHolder.getRequestAttributes());
	}

	public static void clear() {
		getRequestAttributes().ifPresent(attributes -> {
			attributes.removeAttribute(TENANT_ID_KEY, SCOPE_REQUEST);
		});
	}

}