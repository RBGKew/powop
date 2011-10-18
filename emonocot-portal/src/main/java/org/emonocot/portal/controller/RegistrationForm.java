package org.emonocot.portal.controller;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author ben
 *
 */
public class RegistrationForm {

    /**
     *
     */
    @NotEmpty
    @Email
    private String username;

    /**
     *
     */
    @NotEmpty
    @Email
    private String repeatUsername;

    /**
     *
     */
    @NotEmpty
    private String repeatPassword;

    /**
     *
     */
    @NotEmpty
    private String password;

    /**
     *
     * @return the username
     */
    public final String getUsername() {
        return username;
    }

    /**
     *
     * @return the password
     */
    public final String getPassword() {
        return password;
    }

    /**
     * @return the repeatUsername
     */
    public final String getRepeatUsername() {
        return repeatUsername;
    }

    /**
     * @param newRepeatUsername the repeatUsername to set
     */
    public final void setRepeatUsername(final String newRepeatUsername) {
        this.repeatUsername = newRepeatUsername;
    }

    /**
     * @return the repeatPassword
     */
    public final String getRepeatPassword() {
        return repeatPassword;
    }

    /**
     * @param newRepeatPassword the repeatPassword to set
     */
    public final void setRepeatPassword(final String newRepeatPassword) {
        this.repeatPassword = newRepeatPassword;
    }

    /**
     * @param newUsername the username to set
     */
    public final void setUsername(final String newUsername) {
        this.username = newUsername;
    }

    /**
     * @param newPassword the password to set
     */
    public final void setPassword(final String newPassword) {
        this.password = newPassword;
    }

}
