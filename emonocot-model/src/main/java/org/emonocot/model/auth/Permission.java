package org.emonocot.model.auth;

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
    PERMISSION_DELETE_SOURCE,
    /**
     *
     */
    PERMISSION_CREATE_JOBEXECUTION,
    /**
     *
     */
    PERMISSION_DELETE_JOBEXECUTION,
    /**
     *
     */
    PERMISSION_CREATE_JOBINSTANCE,
    /**
     *
     */
    PERMISSION_DELETE_JOBINSTANCE,
    /**
     *
     */
    PERMISSION_CREATE_ANNOTATION,
    /**
     *
     */
    PERMISSION_DELETE_ANNOTATION,
    /**
     *
     */
    PERMISSION_ADMINISTRATE,
    /**
     *
     */
    PERMISSION_WRITE_GROUP,
    /**
    *
    */
    PERMISSION_DELETE_COMMENT;

    /**
     * @return the authority
     */
    public String getAuthority() {
        return this.name();
    }

}
