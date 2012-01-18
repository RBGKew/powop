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
public class SourceUpdatePage extends PageObject {

   /**
    *
    */
    @FindBy(how = How.ID, using = "user")
    private WebElement userForm;

   /**
    *
    */
    @FindBy(how = How.ID, using = "ace")
    private WebElement aceForm;

    /**
     *
     * @param identifier Set the identifier
     */
    public final void setMember(final String identifier) {
        userForm.findElement(By.name("username")).sendKeys(identifier);
    }

   /**
    *
    * @return the current page
    */
   public final PageObject submitMemberForm() {
       userForm.submit();
       return getPage(SourceUpdatePage.class);
   }

   /**
    *
    * @param object Set the object
    */
    public final void setSecureObject(final String object) {
        aceForm.findElement(By.name("object")).sendKeys(object);
    }

    /**
    *
    * @return the current page
    */
   public final PageObject submitAceForm() {
       aceForm.submit();
       return getPage(SourceUpdatePage.class);
   }
}
