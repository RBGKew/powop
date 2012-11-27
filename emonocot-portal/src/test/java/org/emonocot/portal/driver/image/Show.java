package org.emonocot.portal.driver.image;

import org.emonocot.portal.driver.IllustratedPage;
import org.emonocot.portal.driver.PageObject;
import org.emonocot.portal.driver.Search;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class Show extends PageObject implements IllustratedPage {

    /**
     *
     */
    @FindBy(how = How.ID, using = "main-img")
    private WebElement mainImage;

    /**
     *
     */
    @FindBy(how = How.ID, using = "page-title")
    private WebElement caption;

   /**
    *
    */
    @FindBy(how = How.ID, using = "image-properties")
    private WebElement properties;

   /**
    *
    */
    @FindBy(how = How.ID, using = "keywords")
    private WebElement keywords;

    /**
    *
    */
    @FindBy(how = How.CLASS_NAME, using = "ad-thumb-list")
    private WebElement thumbnailContainer;

    /**
     * @param property
     *            the property to get
     * @return the value of the main image propery
     */
    public final String getMainImageProperty(final String property) {
        WebElement value = properties.findElement(By
                .xpath("tbody/tr[td/text() = '" + property + "']/td[2]"));
        return value.getText();
    }

    /**
     * @return the url of the main image
     */
    public final String getMainImage() {
    	String src = mainImage.getAttribute("src");
    	return src.substring(src.lastIndexOf("/") + 1);      
    }

    /**
     * @return the thumbnails
     */
    public final int getThumbnails() {
        return thumbnailContainer.findElements(By.tagName("li")).size();
    }

    /**
     *
     * @param keyword Set the keyword
     * @return the current page
     */
    public final Search selectKeyword(final String keyword) {
        WebElement keywordElement = keywords.findElement(By.xpath("a[@title = '"
                + keyword + "']"));
        return this.openAs(keywordElement.getAttribute("href"),
                Search.class);
    }
}
