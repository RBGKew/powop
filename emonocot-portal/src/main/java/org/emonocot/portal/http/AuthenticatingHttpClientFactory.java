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
package org.emonocot.portal.http;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.emonocot.model.auth.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author ben
 *
 */
public class AuthenticatingHttpClientFactory extends HttpComponentsClientHttpRequestFactory {

	/**
	 * @param uri Set the uri
	 * @param httpMethod set the httpMethod
	 * @return a client http request object
	 * @throws IOException if there is a problem
	 */
	public final ClientHttpRequest createRequest(final URI uri,
			final HttpMethod httpMethod) throws IOException {
		ClientHttpRequest clientHttpRequest = super.createRequest(uri,
				httpMethod);
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext != null
				&& securityContext.getAuthentication() != null) {
			Authentication authentication = securityContext.getAuthentication();
			if (authentication != null
					&& authentication.getPrincipal() != null
					&& authentication.getPrincipal().getClass()
					.equals(User.class)) {
				User user = (User) authentication.getPrincipal();
				String unencoded = user.getUsername() + ":"
						+ user.getPassword();
				String encoded = new String(Base64.encodeBase64(unencoded.getBytes()));
				clientHttpRequest.getHeaders().add("Authorization",
						"Basic " + encoded);
			}
		}
		return clientHttpRequest;
	}

	public final void setProxy(HttpHost proxy){
		super.getHttpClient().getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}
}
