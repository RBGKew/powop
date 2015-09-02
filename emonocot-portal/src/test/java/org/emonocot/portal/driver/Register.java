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

import org.emonocot.model.auth.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class Register extends PageObject {

	/**
	 *
	 */
	private String username = null;

	/**
	 *
	 */
	@FindBy(how = How.ID, using = "registrationForm")
	private WebElement registrationForm;

	/**
	 *
	 * @param username Set the username
	 */
	public final void setUsername(final String username) {
		this.username = username;
		registrationForm.findElement(By.name("username")).sendKeys(username);
	}

	/**
	 *
	 * @param repeatUsername Set the repeat username
	 */
	public final void setRepeatUsername(final String repeatUsername) {
		registrationForm.findElement(By.name("repeatUsername")).sendKeys(repeatUsername);
	}

	/**
	 *
	 * @param password Set the password
	 */
	public final void setPassword(final String password) {
		registrationForm.findElement(By.name("password")).sendKeys(password);
	}

	/**
	 *
	 * @param repeatPassword Set the repeat password
	 */
	public final void setRepeatPassword(final String repeatPassword) {
		registrationForm.findElement(By.name("repeatPassword")).sendKeys(repeatPassword);
	}

	/**
	 *
	 * @return the response page
	 */
	public final PageObject submit() {
		registrationForm.submit();
		if (getWebDriver().getCurrentUrl().equals(
				this.getBaseUri() + "/register")) {
			username = null;
			return super.getPage(Register.class);
		} else {
			User user = new User();
			user.setUsername(username);
			testDataManager.registerObject(user);
			return super.getPage(Login.class);
		}
	}

}
