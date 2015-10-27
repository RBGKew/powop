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

import java.util.List;

import org.emonocot.model.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class AbstractUpdateForm extends PageObject {

	protected String formId;

	protected Class<? extends Base> objectClass;

	protected Class<? extends PageObject> onSubmitPageClass;


	/**
	 * @return the current page
	 */
	public PageObject submit() {
		getForm().submit();
		return getPage(onSubmitPageClass);
	}

	protected abstract WebElement getForm();

	/**
	 * @param fieldName Set the fieldName
	 * @param fieldValue Set the fieldValue
	 */
	public final void setFormField(String fieldName, String fieldValue) {
		getForm().findElement(By.name(fieldName)).clear();
		getForm().findElement(By.name(fieldName)).sendKeys(fieldValue);
	}

	/**
	 * @param fieldName Set the fieldName
	 * @param fieldValue Set the fieldValue
	 */
	public final void setFormSelection(String fieldName, String fieldValue) {
		WebElement select = getForm().findElement(By.name(fieldName));
		List<WebElement> options = select.findElements(By.tagName("option"));
		for(WebElement option : options){
			if(option.getText()== fieldValue){
				option.click();
				break;
			}
		}
	}
}