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
    private LoginPage loginPage;
    /**
     *
     * @param newLoginPage Set the login page
     */
    public RequiresLoginException(final LoginPage newLoginPage) {
        this.loginPage = newLoginPage;
    }

    /**
     * @return the login page
     */
    public final LoginPage getLoginPage() {
        return loginPage;
    }
}
