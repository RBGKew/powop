package org.emonocot.portal.driver;

import java.util.List;

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
   @FindBy(how = How.CLASS_NAME, using = "ancestorsList")
   private List<WebElement> ancestors;

   /**
   *
   */
   @FindBy(how = How.CLASS_NAME, using = "childrenList")
   private WebElement children;

   /**
   *
   */
   @FindBy(how = How.CLASS_NAME, using = "childrenList")
   private List<WebElement> subordinateNumber;

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
                .xpath("div[div/h2 = '" + heading + "']/p"));
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
                .xpath("div[div/h2 = '" + heading + "']/p"));
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
     * @param property Set the property to get
     * @return the caption of the main image
     */
    public final String getMainImageProperty(final String property) {
        return mainImage.findElement(By.className("ad-image-description"))
                .getText();
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

    /**
     *
     * @param thumbnail Set the thumbnail
     */
    public final void selectThumbnail(final Integer thumbnail) {
        List<WebElement> thumbnails = thumbnailContainer.findElements(By
                .xpath("li/a"));
        thumbnails.get(thumbnail - 1).click();
    }

    /**
     *
     * @return the image page
     */
    public final ImagePage selectMainImage() {
        String link = mainImage.findElement(
                By.xpath("div[@class='ad-image']/a")).getAttribute("href");
        return openAs(link, ImagePage.class);
    }

    /**
     *
     * @param thumbnail Set the thumbnail number
     * @return the caption
     */
    public final String getThumbnailCaption(final int thumbnail) {
        List<WebElement> thumbnails = thumbnailContainer.findElements(By
                .xpath("li/a/img"));
        return thumbnails.get(thumbnail - 1).getAttribute("title");
    }

   /**
    *
    * @param thumbnail Set the thumbnail number
    * @return the image
    */
    public final String getThumbnailImage(final int thumbnail) {
        List<WebElement> thumbnails = thumbnailContainer.findElements(By
                .xpath("li/a"));
        return thumbnails.get(thumbnail - 1).getAttribute("href");
    }

    /**
     *
     * @return the number of ancestors
     */
    public final Integer getAncestorsNumber() {
        return ancestors.size();
    }

    /**
     *
     * @return the subordinate taxa
     */
    public final String getSubordinateTaxa() {
        return children.getText();
    }

    /**
     *
     * @return the number of children
     */
    public final Integer getChildrenNumber() {
        return subordinateNumber.size();
    }

    /**
     *
     * @param citeKey The citation key of the bibliography entry of interest
     * @return the bibliography entry
     */
    public final String getBibliographyEntry(final String citeKey) {
        WebElement bibliography = webDriver.findElement(By.id("bibliography"));
        WebElement element = bibliography.findElement(By
                .xpath("ul/li[a = '" + citeKey + "']"));
        return element.getText();
    }

    /**
     *
     * @param topic The text topic
     * @return the citations for that topic
     */
    public final String getCitations(final String topic) {
        WebElement element = textContent.findElement(By
                .xpath("div[div/h2 = '" + topic + "']/ul[@class='citations']"));
        return element.getText();
    }

    /**
     *
     * @return the protolog link
     */
    public final String getProtologueLink() {
        WebElement link = protologue.findElement(By.tagName("a"));
        return link.getAttribute("href");
    }

}
