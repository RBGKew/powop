package org.emonocot.portal.driver;

import java.util.ArrayList;
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
public class SearchResultsPage extends PageObject {
   /**
    *
    */
   @FindBy(how = How.ID, using = "results")
   private WebElement results;

   /**
    *
    */
   @FindBy(how = How.ID, using = "pages")
   private WebElement message;

   /**
   *
   */
  @FindBy(how = How.ID, using = "CLASS")
  private WebElement classFacets;

   /**
    *
    * @return the number of results
    */
    public final Integer getResultNumber() {
        return results.findElements(By.tagName("li")).size();
    }

    /**
     *
     * @return the number of class facets
     */
    public final Integer getClassFacetNumber() {
        return classFacets.findElements(By.xpath("ul/li")).size();
    }

    /**
     *
     * @return an array of the class facet labels
     */
    public final String[] getClassFacets() {
        List<WebElement> classFacetOptions = classFacets.findElements(By
                .xpath("ul/li"));
        String[] result = new String[classFacetOptions.size()];
        for (int i = 0; i < result.length; i++) {
            WebElement classFacetOption = classFacetOptions.get(i);

            try {
                result[i] = classFacetOption.findElement(By.tagName("a"))
                        .getText();
            } catch (NoSuchElementException nsee) {
                result[i] = classFacetOption.getText();
            }
        }
        return result;
    }

    /**
     *
     * @param clazz the name of the class to facet on
     * @return the corresponding search results page
     */
    public final SearchResultsPage selectClassFacet(final String clazz) {
        WebElement classFacet = classFacets.findElement(By
                .xpath("ul/li/a[text() = \'" + clazz + "\']"));
        return openAs(classFacet.getAttribute("href"),
                SearchResultsPage.class);
    }

    /**
     *
     * @return an array of results
     */
    public final List<String[]> getResults() {
        List<WebElement> links = results.findElements(By.xpath("li/a"));
        List<String[]> linksList = new ArrayList<String[]>();
        for (WebElement webElement : links) {
            String[] link = new String[2];
            String href = webElement.getAttribute("href");
            link[0] = href.substring(href.lastIndexOf("/") + 1);
            link[1] = webElement.getText();
            linksList.add(link);
        }
        return linksList;
    }

    /**
     *
     * @return the message from the search results page
     */
    public final String getMessage() {
        return message.getText();
    }

}
