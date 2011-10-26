package org.emonocot.portal.driver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class SourcePage extends PageObject {
	
	/**
    *
    */
   @FindBy(how = How.XPATH, using = "//div[@class='content-wrapper']/a")
   private WebElement link;
   
   /**
   *
   */
  @FindBy(how = How.ID, using = "page-title")
  private WebElement name;

	public String getLink() {
		return link.getAttribute("href");
	}

	public String getName() {
		return name.getText();
	}

}
