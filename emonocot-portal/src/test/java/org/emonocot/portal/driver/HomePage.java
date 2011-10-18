package org.emonocot.portal.driver;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class HomePage extends PageObject {

   /**
    *
    */
   @FindBy(how = How.LINK_TEXT, using = "Register")
   private WebElement register;

   /**
    *
    * @return the registration page
    */
    public final RegistrationPage selectRegistrationLink() {
        return super.openAs(register.getAttribute("href"),
                RegistrationPage.class);
    }

}
