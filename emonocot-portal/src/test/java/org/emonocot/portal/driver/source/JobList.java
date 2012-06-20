package org.emonocot.portal.driver.source;

import org.emonocot.portal.driver.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author annapaola
 *
 */
public class JobList extends PageObject {

    /**
   *
   */
    @FindBy(how = How.ID, using = "page-title")
    private WebElement title;
}
