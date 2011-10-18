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
    PERMISSION_DELETE_REFERENCE,
    /**
     *
     */
   PERMISSION_CREATE_USER,
    /**
     *
     */
    PERMISSION_DELETE_USER,
    /**
     *
     */
    PERMISSION_CREATE_GROUP,
    /**
     *
     */
    PERMISSION_DELETE_GROUP,
    /**
     *
     */
    PERMISSION_VIEW_SOURCE,
    /**
     *
     */
    PERMISSION_CREATE_SOURCE,
    /**
     *
     */
    PERMISSION_DELETE_SOURCE;

    /**
     * @return the authority
     */
    public String getAuthority() {
        return this.name();
    }

}
