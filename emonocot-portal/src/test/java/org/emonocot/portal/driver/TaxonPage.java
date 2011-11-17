package org.emonocot.portal.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class TaxonPage extends PageObject implements IllustratedPage {

    /**
     *
     */
    @FindBy(how = How.ID, using = "page-title")
    private WebElement title;

    /**
     *
     */
    @FindBy(how = How.ID, using = "textContent")
    private WebElement textContent;

    /**
     *
     */
    @FindBy(how = How.ID, using = "protologue")
    private WebElement protologue;

    /**
     *
     */
    @FindBy(how = How.CLASS_NAME, using = "ad-image-wrapper")
    private WebElement mainImage;

    /**
     *
     */
    @FindBy(how = How.CLASS_NAME, using = "ad-thumb-list")
    private WebElement thumbnailContainer;

   /**
    *
    */
   @FindBy(how = How.ID, using = "alternative-map")
   private WebElement map;

    /**
     *
     * @return the page title
     */
    public final String getTaxonName() {
        return title.findElement(By.xpath("span")).getText();
    }

    /**
     *
     * @param attribute Set the CSS attribute you're interested in
     * @return the class of the page title
     */
    public final String getTaxonNameStyle(final String attribute) {
        return title.findElement(By.xpath("span")).getCssValue(attribute);
    }

    /**
     *
     * @param heading Set the heading
     * @return a paragraph with that title
     */
    public final String getParagraph(final String heading) {
        WebElement element = textContent.findElement(By
                .xpath("div[h5 = '" + heading + "']/p"));
        return element.getText();
    }

    /**
     *
     * @param heading Set the heading
     * @return true if paragraph exists, false otherwise
     */
    public final boolean doesParagraphExist(final String heading) {
        try {
            WebElement element = textContent.findElement(By
                .xpath("div/div[h5 = '" + heading + "']/p"));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return the protologue
     */
    public final String getProtologue() {
        return protologue.getText();
    }

    /**
     *
     * @return the caption of the main image
     */
    public final String getMainImageCaption() {
        return mainImage.findElement(By.className("ad-image-description")).getText();
    }

    /**
     *
     * @return the url of the main image
     */
    public final String getMainImage() {
        return mainImage.findElement(By.tagName("img")).getAttribute("src");
    }

    /**
     *
     * @return the number of thumbnails
     */
    public final int getThumbnails() {
        return thumbnailContainer.findElements(
                By.tagName("li")).size();
    }

    /**
     *
     * @return the url of the distribution map
     */
    public final String getDistributionMap() {
        return map.getAttribute("src");
    }

}
