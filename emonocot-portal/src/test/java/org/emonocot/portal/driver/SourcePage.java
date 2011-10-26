package org.emonocot.portal.driver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author annapaola
 *
 */
public class SourcePage extends PageObject {

   /**
    *
    */
   @FindBy(how = How.XPATH, using = "//div[@class='content-wrapper']/a[1]")
   private WebElement link;

   /**
   *
   */
  @FindBy(how = How.ID, using = "page-title")
  private WebElement name;

    /**
     *
     * @return the link
     */
    public final String getLink() {
        return link.getAttribute("href");
    }

    /**
     *
     * @return the name
     */
    public final String getName() {
        return name.getText();
    }

}
