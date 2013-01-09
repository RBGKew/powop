package org.emonocot.portal.driver.resource;

import org.emonocot.portal.driver.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class List extends PageObject {
	
	@FindBy(how = How.ID, using = "resources")
	private WebElement jobs;

	/**
	 *
	 * @return the number of jobs listed
	 */
	public Integer getNumberOfJobs() {
		return jobs.findElements(By.tagName("li")).size();
	}

}
