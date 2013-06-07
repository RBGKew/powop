package org.emonocot.portal.auth;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class RestAuthenticationToken extends AbstractAuthenticationToken {
	
	private Object principal;
	
	private Object credentials;
	
	private UserDetails userDetails;

	public RestAuthenticationToken(
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
	}

	public RestAuthenticationToken(Object principal,
			Object credentials) {
		super(null);
		this.principal = principal;
		this.credentials = credentials;
	}

	public RestAuthenticationToken(Object principal, Object credentials,
			UserDetails userDetails) {
		super(userDetails.getAuthorities());
		this.principal = principal;
		this.credentials = credentials;
		this.userDetails = userDetails;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4121755228222723685L;

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}
	
	public Object getDetails() {
		return userDetails;
	}
}
