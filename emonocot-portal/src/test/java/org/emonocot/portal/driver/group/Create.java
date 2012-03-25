package org.emonocot.portal.driver.group;

import org.emonocot.model.user.Group;
import org.emonocot.portal.driver.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class Create extends PageObject {

   /**
    *
    */
    @FindBy(how = How.ID, using = "group")
    private WebElement createGroupForm;

    /**
     *
     */
    private String groupName = null;

    /**
     *
     * @param identifier Set the identifier
     */
    public final void setGroupName(final String identifier) {
        this.groupName = identifier;
        createGroupForm.findElement(By.name("name")).sendKeys(identifier);
    }

    /**
    *
    * @return the current page
    */
   public final PageObject submit() {
       createGroupForm.submit();
       if (groupName != null) {
           Group group = new Group();
           group.setIdentifier(groupName);
           testDataManager.registerObject(group);
           this.groupName = null;
       }
       return getPage(Show.class);
   }

}
