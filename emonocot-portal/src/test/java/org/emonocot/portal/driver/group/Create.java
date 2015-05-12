/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.portal.driver.group;

import org.emonocot.model.auth.Group;
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
