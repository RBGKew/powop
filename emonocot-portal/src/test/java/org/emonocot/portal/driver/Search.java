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
public class Search extends PageObject {
    /**
     *
     */
    @FindBy(how = How.ID, using = "results")
    private WebElement results;

    /**
     *
     */
    @FindBy(how = How.CLASS_NAME, using = "ui-autocomplete")
    private WebElement autocomplete;

    /**
     *
     */
    @FindBy(how = How.ID, using = "query")
    private WebElement query;

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
    */
   @FindBy(how = How.ID, using = "sorting")
   private WebElement sorting;


    /**
     *
     */
    @FindBy(how = How.ID, using = "viewIcons")
    private WebElement viewIcons;

    /**
     *
     * @return the number of results
     */
    public final Integer getResultNumber() {
        return results.findElements(By.className("result")).size();
    }

    /**
     * @param facetName
     *            set the facet name
     * @return the number of class facets
     */
    public final Integer getFacetNumber(final String facetName) {
        return facets.findElements(
                By.xpath("li[@class = '" + facetName + "']/a")).size();
    }

    /**
     *
     * @param sort
     *            The string to sort by
     * @return a search results page
     */
    public final Search sort(final String sort) {
        WebElement classFacet = sorting.findElement(By
                .xpath("li/a[text() = \'" + sort
                        + "\']"));
        return openAs(classFacet.getAttribute("href"), Search.class);
    }

    /**
     *
     * @param grid
     *            Go to the grid view
     * @return a search results page
     */
    public final Search view(final String grid) {
        WebElement idViewIcon = viewIcons.findElement(By
                .xpath("div/a[@title = \'" + grid + "\']"));
        return openAs(idViewIcon.getAttribute("href"), Search.class);
    }

    /**
     * @param facetName
     *            Set the facet name
     * @return an array of the facet labels
     */
    public final String[] getFacets(final String facetName) {
        List<WebElement> facetOptions = facets.findElements(By
                .xpath("li[@class = '" + facetName + "']"));
        String[] result = new String[facetOptions.size()];
        for (int i = 0; i < result.length; i++) {
            WebElement classFacetOption = facetOptions.get(i);

            try {
                result[i] = classFacetOption.findElement(By.className("facetValue"))
                        .getText();
            } catch (NoSuchElementException nsee) {
                result[i] = classFacetOption.getText();
            }
        }
        return result;
    }

    /**
     * @return an array of the autocomplete options
     */
    public final String[] getAutocompleteOptions() {
        List<WebElement> autocompleteOptions = autocomplete.findElements(By
                .xpath("li/a"));
        String[] result = new String[autocompleteOptions.size()];
        for (int i = 0; i < result.length; i++) {
            WebElement autocompleteOption = autocompleteOptions.get(i);
            result[i] = autocompleteOption.getText();
        }
        return result;
    }

    /**
     * @param facetName
     *            the name of the facet
     * @param facetValue
     *            the name of the facet value to select
     * @return the corresponding search results page
     */
    public final Search selectFacet(final String facetName,
            final String facetValue) {
        WebElement classFacet = facets.findElement(By.xpath("li[@class = '" + facetName + "']/a[span/text() = \'" + facetValue + "\']"));
        return openAs(classFacet.getAttribute("href"), Search.class);
    }

    /**
     *
     * @return an array of results
     */
    public final List<String[]> getResults() {
        List<WebElement> links = results.findElements(By.className("result"));
        List<String[]> linksList = new ArrayList<String[]>();
        for (WebElement webElement : links) {
            String[] link = new String[2];
            String href = webElement.getAttribute("href");
            link[0] = href.substring(href.lastIndexOf("/") + 1);
            link[1] = webElement.getAttribute("title");
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

    /**
     *
     * @param queryString
     *            Set the query on the search results page
     */
    public final void setQuery(final String queryString) {
        query.sendKeys(queryString);
    }

    /**
     *
     * @return true if the icons exist, false otherwise
     */
    public final Boolean viewIconDisplay() {
        try {
            WebElement element = viewIcons.findElement(By.tagName("div"));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return true if the results are displayed in a grid
     */
    public final boolean resultsAreDisplayedInGrid() {
        try {
            WebElement element = results.findElement(By.xpath("ul/li"));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;

    }

}
