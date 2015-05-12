/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
