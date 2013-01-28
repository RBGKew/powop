package org.emonocot.portal.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class Login extends PageObject {

   /**
    *
    */
    @FindBy(how = How.ID, using = "loginForm")
    private WebElement loginForm;
    
    @FindBy(how = How.LINK_TEXT, using = "Create a new account")
    private WebElement registerLink;

    /**
     *
     * @param username
     *            Set the username
     */
    public final void setUsername(final String username) {
        loginForm.findElement(By.name("j_username")).sendKeys(username);
    }

    /**
     *
     * @param password
     *            Set the password
     */
    public final void setPassword(final String password) {
        loginForm.findElement(By.name("j_password")).sendKeys(password);
    }

    /**
     *
     * @return the response page
     */
    public final PageObject submit() {
        loginForm.submit();
        return openAs(getWebDriver().getCurrentUrl(), Profile.class);
    }
    
    /**
     * @return the registration page
     */
    public final Register selectRegistrationLink() {
        return openAs(registerLink.getAttribute("href"), Register.class);
    }
}
