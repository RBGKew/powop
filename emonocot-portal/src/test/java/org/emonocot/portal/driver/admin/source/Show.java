package org.emonocot.portal.driver.admin.source;

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
public class Show extends PageObject {
	@FindBy(how = How.ID, using = "jobs")
	private WebElement jobs;

	/**
	 *
	 * @return the number of jobs listed
	 */
	public Integer getNumberOfJobs() {
		return jobs.findElements(By.tagName("tr")).size();
	}

}
