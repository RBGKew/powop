package org.emonocot.portal.driver.source;

import org.emonocot.model.Source;
import org.emonocot.portal.driver.AbstractUpdateForm;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class Update extends AbstractUpdateForm {
	
	@FindBy(how = How.ID, using = "source")
	private WebElement form;

	public Update() {
    	objectClass = Source.class;
    	onSubmitPageClass = org.emonocot.portal.driver.source.Update.class;
    }

	@Override
	protected WebElement getForm() {	
		return form;
	}
}
