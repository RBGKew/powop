package org.emonocot.portal.controller;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author ben
 *
 */
public class LoginForm {

    /**
     *
     */
    @NotEmpty
    private String j_password;

    /**
     *
     */
    @NotEmpty
    private String j_username;

    /**
     * @return the j_password
     */
    public final String getJ_password() {
        return j_password;
    }

    /**
     * @param j_password the j_password to set
     */
    public final void setJ_password(String j_password) {
        this.j_password = j_password;
    }

    /**
     * @return the j_username
     */
    public final String getJ_username() {
        return j_username;
    }

    /**
     * @param j_username the j_username to set
     */
    public final void setJ_username(String j_username) {
        this.j_username = j_username;
    }
}
