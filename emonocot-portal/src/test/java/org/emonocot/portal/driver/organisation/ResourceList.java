package org.emonocot.portal.driver.organisation;

import org.emonocot.portal.driver.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author annapaola
 *
 */
public class ResourceList extends PageObject {

    /**
   *
   */
    @FindBy(how = How.ID, using = "page-title")
    private WebElement title;
}
