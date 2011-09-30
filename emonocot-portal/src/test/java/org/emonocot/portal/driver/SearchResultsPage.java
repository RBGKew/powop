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
  @FindBy(how = How.ID, using = "facets")
  private WebElement facets;

   /**
    *
    * @return the number of results
    */
    public final Integer getResultNumber() {
        return results.findElements(By.tagName("li")).size();
    }

    /**
     * @param facetName set the facet name
     * @return the number of class facets
     */
    public final Integer getFacetNumber(final String facetName) {
        return facets.findElements(By.xpath("li[@id = '" + facetName + "']/ul/li")).size();
    }

    /**
     *
     * @param sort The string to sort by
     * @return a search results page
     */
   public final SearchResultsPage sort(final String sort) {
       WebElement classFacet = facets.findElement(By
               .xpath("li[h2/text() = 'Sort']/ul/li/a[text() = \'" + sort + "\']"));
       return openAs(classFacet.getAttribute("href"),
               SearchResultsPage.class);
   }

    /**
     * @param facetName Set the facet name
     * @return an array of the facet labels
     */
    public final String[] getFacets(final String facetName) {
        List<WebElement> facetOptions = facets.findElements(By
                .xpath("li[h2/text() = '" + facetName + "']/ul/li"));
        String[] result = new String[facetOptions.size()];
        for (int i = 0; i < result.length; i++) {
            WebElement classFacetOption = facetOptions.get(i);

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
     * @param facetName the name of the facet
     * @param facetValue the name of the facet value to select
     * @return the corresponding search results page
     */
    public final SearchResultsPage selectFacet(final String facetName,
            final String facetValue) {
        WebElement classFacet = facets.findElement(By
                .xpath("li[h2/text() = '" + facetName + "']/ul/li/a[text() = \'" + facetValue + "\']"));
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
