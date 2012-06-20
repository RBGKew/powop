package org.emonocot.portal.driver.source;

import org.emonocot.model.source.Source;
import org.emonocot.portal.driver.AbstractCreateForm;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class Create extends AbstractCreateForm {

	@FindBy(how = How.ID, using = "source")
	private WebElement form;

	public Create() {
		objectClass = Source.class;
		onSubmitPageClass = org.emonocot.portal.driver.source.Update.class;
	}

	@Override
	protected WebElement getForm() {
		return form;
	}

}
