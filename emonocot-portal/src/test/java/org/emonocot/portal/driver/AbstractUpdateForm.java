package org.emonocot.portal.driver;

import java.util.List;

import org.emonocot.model.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public abstract class AbstractUpdateForm extends PageObject {

	/**
	 *
	 */
	protected String formId;
	/**
	 *
	 */
	protected Class<? extends Base> objectClass;

	/**
	 *
	 */
	protected Class<? extends PageObject> onSubmitPageClass;


	/**
	 *
	 * @return the current page
	 */
	public PageObject submit() {
	    getForm().submit();
	    return getPage(onSubmitPageClass);
	}

	protected abstract WebElement getForm();

	/**
	 *
	 * @param fieldName Set the fieldName
	 * @param fieldValue Set the fieldValue
	 */
	public final void setFormField(String fieldName, String fieldValue) {
		getForm().findElement(By.name(fieldName)).clear();
		getForm().findElement(By.name(fieldName)).sendKeys(fieldValue);
	}
	
	/**
	 *
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