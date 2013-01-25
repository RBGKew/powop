package org.emonocot.portal.controller;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author ben
 *
 */
public class LoginForm {

    @NotEmpty
    private String j_password;

    @NotEmpty
    private String j_username;

    public String getJ_password() {
        return j_password;
    }

    public void setJ_password(String j_password) {
        this.j_password = j_password;
    }

    public String getJ_username() {
        return j_username;
    }

    public void setJ_username(String j_username) {
        this.j_username = j_username;
    }
}
