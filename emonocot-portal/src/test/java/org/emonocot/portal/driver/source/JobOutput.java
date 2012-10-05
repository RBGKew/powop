package org.emonocot.portal.driver.source;

import java.util.ArrayList;
import java.util.List;

import org.emonocot.portal.driver.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class JobOutput extends PageObject {

    /**
     *
     */
    @FindBy(how = How.ID, using = "results")
    private WebElement results;
    
    /**
     *
     */
    @FindBy(how = How.ID, using = "facets")
    private WebElement facets;

    /**
     *
     * @return the number of results
     */
    public final int getResultNumber() {
        List<WebElement> rows = results.findElements(By.tagName("tr"));
        return rows.size();
    }

    /**
     *
     * @return the results as an array of strings
     */
    public final List<String[]> getResults() {
        List<String[]> r = new ArrayList<String[]>();
        List<WebElement> rows = results.findElements(By.xpath("tr"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            String[] result = new String[4];
            result[0] = cells.get(0).getText();
            result[1] = cells.get(1).getText();
            result[2] = cells.get(2).getText();
            result[3] = cells.get(3).getText();
            r.add(result);
        }
        return r;
    }

    /**
     *
     * @param facetName TODO
     * @param category Set the category
     * @return the source job page
     */
    public final JobOutput selectFacet(String facetName, final String facetValue) {
    	WebElement facet = facets.findElement(By.xpath("div/div/div/ul/li[@class = '" + facetName + "']/a[span/text() = \'" + facetValue + "\']"));
        return openAs(
                facet.getAttribute("href"),
                JobOutput.class);
    }

}
