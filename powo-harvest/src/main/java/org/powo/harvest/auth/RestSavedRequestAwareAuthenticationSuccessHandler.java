package org.powo.harvest.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

/*
 * From http://www.baeldung.com/securing-a-restful-web-service-with-spring-security.
 * Implements the same login as the default org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler 
 * with the redirect logic is removed
 */
public class RestSavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest == null) {
			clearAuthenticationAttributes(request);
			return;
		}

		String targetUrlParam = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl()
				|| (targetUrlParam != null
				&& StringUtils.hasText(request.getParameter(targetUrlParam)))) {
			requestCache.removeRequest(request, response);
			clearAuthenticationAttributes(request);
			return;
		}

		clearAuthenticationAttributes(request);
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}
}