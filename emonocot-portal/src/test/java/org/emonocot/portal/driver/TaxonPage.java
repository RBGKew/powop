package org.emonocot.portal.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class TaxonPage extends PageObject {

    /**
     *
     */
    @FindBy(how = How.ID, using = "page-title")
    private WebElement title;

    /**
     *
     * @return the page title
     */
    public final String getTaxonName() {
        return title.findElement(By.xpath("span")).getText();
    }

    /**
     *
     * @return the class of the page title
     */
    public final String getTaxonNameClass() {
        return title.findElement(By.xpath("span")).getAttribute("class");
    }

}
