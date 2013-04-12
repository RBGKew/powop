package org.emonocot.portal.driver.phylo;

import org.emonocot.portal.driver.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class Show extends PageObject {
	
    @FindBy(how = How.ID, using = "page-title")
    private WebElement title;

	public String getTitle() {
		return title.getText();
	}

    
}
