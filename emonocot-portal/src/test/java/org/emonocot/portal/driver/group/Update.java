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
public class Update extends PageObject {

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
		return getPage(Update.class);
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
		return getPage(Update.class);
	}
}
