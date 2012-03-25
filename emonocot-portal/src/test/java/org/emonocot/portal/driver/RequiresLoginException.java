package org.emonocot.portal.driver;

/**
 * Thrown when the user is redirected to a login page
 * for authentication.
 *
 * @author ben
 *
 */
public class RequiresLoginException extends RuntimeException {
    /**
    *
    */
   private static final long serialVersionUID = -92240534706858990L;

    /**
     *
     */
    private Login loginPage;
    /**
     *
     * @param newLoginPage Set the login page
     */
    public RequiresLoginException(final Login newLoginPage) {
        this.loginPage = newLoginPage;
    }

    /**
     * @return the login page
     */
    public final Login getLoginPage() {
        return loginPage;
    }
}
