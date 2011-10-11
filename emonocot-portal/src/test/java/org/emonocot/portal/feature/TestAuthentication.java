package org.emonocot.portal.feature;

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
    public final Collection<GrantedAuthority> getAuthorities() {
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
