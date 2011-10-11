package org.emonocot.model.user;

import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author ben
 *
 */
public enum Permission implements GrantedAuthority {
    /**
     *
     */
    PERMISSION_CREATE_TAXON,
    /**
     *
     */
    PERMISSION_DELETE_TAXON,
    /**
     *
     */
    PERMISSION_CREATE_IMAGE,
    /**
     *
     */
    PERMISSION_DELETE_IMAGE,
    /**
     *
     */
    PERMISSION_CREATE_REFERENCE,
    /**
     *
     */
    PERMISSION_DELETE_REFERENCE;

    /**
     * @return the authority
     */
    public String getAuthority() {
        return this.name();
    }

}
