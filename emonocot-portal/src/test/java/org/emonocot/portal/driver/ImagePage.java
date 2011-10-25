package org.emonocot.portal.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ImagePage extends PageObject implements IllustratedPage {

	@FindBy(how = How.ID, using = "main-img")
	private WebElement mainImage;
	
	@FindBy(how = How.ID, using = "page-title")
	private WebElement caption;

	/**
    *
    */
   @FindBy(how = How.CLASS_NAME, using = "ad-thumb-list")
   private WebElement thumbnailContainer;
   
   
	public String getMainImageCaption() {
		return caption.getText();
	}

	public String getMainImage() {
		return mainImage.getAttribute("src");
	}

	public int getThumbnails() {
		return thumbnailContainer.findElements(
                By.tagName("li")).size();
	}

}
