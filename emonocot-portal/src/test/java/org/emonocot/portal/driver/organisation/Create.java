package org.emonocot.portal.driver.organisation;

import org.emonocot.model.registry.Organisation;
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

	@FindBy(how = How.ID, using = "organisation")
	private WebElement form;

	public Create() {
		objectClass = Organisation.class;
		onSubmitPageClass = org.emonocot.portal.driver.organisation.Update.class;
	}

	@Override
	protected WebElement getForm() {
		return form;
	}

}
