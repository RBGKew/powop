package org.emonocot.portal.driver;

import java.io.IOException;
import java.net.URI;

import org.apache.catalina.util.Base64;
import org.emonocot.model.user.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author ben
 *
 */
public class AuthenticatingHttpClientFactory extends
        SimpleClientHttpRequestFactory {

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
                String encoded = new String(Base64.encode(unencoded.getBytes()));
                clientHttpRequest.getHeaders().add("Authorization",
                        "Basic " + encoded);
            }
        }
        return clientHttpRequest;
    }
}
