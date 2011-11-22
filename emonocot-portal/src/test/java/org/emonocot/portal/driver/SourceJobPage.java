package org.emonocot.portal.driver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author ben
 *
 */
public class SourceJobPage extends PageObject {

    /**
     *
     */
    @FindBy(how = How.ID, using = "results")
    private WebElement results;

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
            String[] result = new String[2];
            result[0] = cells.get(0).getText();
            result[1] = cells.get(1).getText();
            r.add(result);
        }
        return r;
    }

    /**
     *
     * @param category Set the category
     * @return the source job page
     */
    public final SourceJobPage selectCategory(final String category) {
        WebElement link = results.findElement(By.xpath("tr/td/a[text() = '"
                + category + "']"));
        return openAs(
                link.getAttribute("href"),
                SourceJobPage.class);
    }

}
