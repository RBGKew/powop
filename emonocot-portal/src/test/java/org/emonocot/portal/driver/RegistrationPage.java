package org.emonocot.portal.driver;

import org.emonocot.model.user.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class RegistrationPage extends PageObject {

    /**
     *
     */
    @FindBy(how = How.ID, using = "registrationForm")
    private WebElement registrationForm;

    /**
    *
    * @param username Set the username
    */
   public final void setUsername(final String username) {
       registrationForm.findElement(By.name("username")).sendKeys(username);
   }

   /**
    *
    * @param repeatUsername Set the repeat username
    */
   public final void setRepeatUsername(final String repeatUsername) {
       registrationForm.findElement(By.name("repeatUsername")).sendKeys(repeatUsername);
   }

   /**
    *
    * @param password Set the password
    */
   public final void setPassword(final String password) {
       registrationForm.findElement(By.name("password")).sendKeys(password);
   }

   /**
    *
    * @param repeatPassword Set the repeat password
    */
   public final void setRepeatPassword(final String repeatPassword) {
       registrationForm.findElement(By.name("repeatPassword")).sendKeys(repeatPassword);
   }

   /**
    *
    * @return the response page
    */
   public final PageObject submit() {
       registrationForm.submit();
       if (getWebDriver().getCurrentUrl().equals(this.getBaseUri() + "/register")) {
           return super.openAs(getWebDriver().getCurrentUrl(),
                   RegistrationPage.class);
       } else {
           String url = getWebDriver().getCurrentUrl();
           String identifier = url.substring(url.lastIndexOf("/") + 1);
           User user = new User();
           user.setUsername(identifier);
           testDataManager.registerObject(user);
           return super.openAs(getWebDriver().getCurrentUrl(), ProfilePage.class);
       }
   }

}
