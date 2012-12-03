package org.emonocot.portal.driver.organisation;

import java.util.List;

import org.emonocot.portal.driver.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author annapaola
 *
 */
public class Show extends PageObject {

    /**
   *
   */
    @FindBy(how = How.ID, using = "page-title")
    private WebElement title;

    /**
     *
     */
    @FindBy(how = How.ID, using = "organisation-uri")
    private WebElement uri;

    /**
     *
     * @return the title
     */
    public final String getTitle() {
        return title.getText();
    }

    /**
     *
     * @return the uri
     */
    public final String getOrganisationUri() {
        return uri.getText();
    }

    /**
     *
     * @return the source logo
     */
    public final String getOrganisationLogo() {
        return getWebDriver().findElement(By.id("organisation-logo")).getAttribute(
                "src");
    }

    /**
     *
     * @return the number of jobs listed
     */
    public final Integer getResourcesListed() {
        WebElement resources = getWebDriver().findElement(By.id("resources"));
        List<WebElement> rows = resources.findElements(By.tagName("tr"));
        return rows.size();
    }

    /**
     *
     * @param job
     *            Set the job
     * @return the source job page
     */
    public final ResourceOutput selectResource(final int job) {
        WebElement jobs = getWebDriver().findElement(By.id("resources"));
        List<WebElement> list = jobs.findElements(By.xpath("tr/td[10]/a"));
        return openAs(list.get(job - 1).getAttribute("href"),
                ResourceOutput.class);
    }
}
