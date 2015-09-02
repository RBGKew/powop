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
package org.emonocot.test;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author ben
 *
 */
public class TestAuthentication implements Authentication {

	/**
	 *
	 */
	private static final long serialVersionUID = 2714825162771967378L;

	/**
	 *
	 */
	private UserDetails userDetails;
	/**
	 *
	 */
	private boolean authentication = true;

	/**
	 *
	 * @param newUserDetails Set the user details
	 */
	public TestAuthentication(final UserDetails newUserDetails) {
		this.userDetails = newUserDetails;
	}

	/**
	 *
	 * @param newUserDetails Set the user details
	 * @param newAuthentication Set the authentication
	 */
	public TestAuthentication(final UserDetails newUserDetails,
			final boolean newAuthentication) {
		this.userDetails = newUserDetails;
		this.authentication = newAuthentication;
	}

	/**
	 * @return the authorities
	 */
	public final Collection<? extends GrantedAuthority> getAuthorities() {
		return userDetails.getAuthorities();
	}

	/**
	 * @return the credentials
	 */
	public final Object getCredentials() {
		return null;
	}

	/**
	 * @return the details
	 */
	public final Object getDetails() {
		return null;
	}

	/**
	 * @return the principal
	 */
	public final Object getPrincipal() {
		return this.userDetails;
	}

	/**
	 * @return true if the principal is authenticated
	 */
	public final boolean isAuthenticated() {
		return authentication;
	}

	/**
	 * @param newAuthentication set whether the principal is authenticated
	 */
	public final void setAuthenticated(final boolean newAuthentication) {
		this.authentication = newAuthentication;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return null;
	}
}
