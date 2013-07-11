package org.emonocot.portal.auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class RestAPIKeyAuthenticationFilter extends
		AbstractAuthenticationProcessingFilter {
	
	private static final String API_KEY_PARAMETER_NAME = "apikey";
	
	private static Logger logger = LoggerFactory.getLogger(RestAPIKeyAuthenticationFilter.class);

	protected RestAPIKeyAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {
		
		String apiKeyValue = decodeParameterValue(request, API_KEY_PARAMETER_NAME);		
		logger.debug("attemptAuthentication " + apiKeyValue);

		AbstractAuthenticationToken authRequest = createAuthenticationToken(
				apiKeyValue, new RestCredentials());

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		chain.doFilter(request, response);
	}

	private String decodeParameterValue(HttpServletRequest request,
			String requestParameterName) throws UnsupportedEncodingException {
		// This is basically to avoid the weird RFC spec when it comes to spaces
		// in the URL and how they are encoded
		return URLDecoder.decode(
				getParameterValue(request, requestParameterName),
				request.getCharacterEncoding()).replaceAll(" ", "+");
	}

	private String getParameterValue(HttpServletRequest request,
			String requestParameterName) {
		return (request.getParameter(requestParameterName) != null) ? request
				.getParameter(requestParameterName) : "";
	}

	/**
	 * Provided so that subclasses may configure what is put into the
	 * authentication request's details property.
	 * 
	 * @param request
	 *            that an authentication request is being created for
	 * @param authRequest
	 *            the authentication request object that should have its details
	 *            set
	 */
	protected void setDetails(HttpServletRequest request,
			AbstractAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource
				.buildDetails(request));
	}

	private AbstractAuthenticationToken createAuthenticationToken(
			String apiKeyValue, RestCredentials restCredentials) {
		return new RestAuthenticationToken(apiKeyValue, restCredentials);
	}

	@Override
	protected boolean requiresAuthentication(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("Requires Authentication " + (request.getParameter(API_KEY_PARAMETER_NAME) != null));
		return request.getParameter(API_KEY_PARAMETER_NAME) != null;
	}

}
