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
public class Login extends PageObject {

	/**
	 *
	 */
	@FindBy(how = How.ID, using = "loginForm")
	private WebElement loginForm;

	@FindBy(how = How.LINK_TEXT, using = "Create a new account")
	private WebElement registerLink;

	/**
	 *
	 * @param username
	 *            Set the username
	 */
	public final void setUsername(final String username) {
		loginForm.findElement(By.name("j_username")).sendKeys(username);
	}

	/**
	 *
	 * @param password
	 *            Set the password
	 */
	public final void setPassword(final String password) {
		loginForm.findElement(By.name("j_password")).sendKeys(password);
	}

	/**
	 *
	 * @return the response page
	 */
	public final PageObject submit() {
		loginForm.submit();
		return openAs(getWebDriver().getCurrentUrl(), Profile.class);
	}

	/**
	 * @return the registration page
	 */
	public final Register selectRegistrationLink() {
		return openAs(registerLink.getAttribute("href"), Register.class);
	}
}
