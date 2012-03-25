package org.emonocot.portal.driver.source;

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
    @FindBy(how = How.ID, using = "source-uri")
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
    public final String getSourceUri() {
        return uri.getText();
    }

    /**
     *
     * @return the source logo
     */
    public final String getSourceLogo() {
        return getWebDriver().findElement(By.id("source-logo")).getAttribute(
                "src");
    }

    /**
     *
     * @return the number of jobs listed
     */
    public final Integer getJobsListed() {
        WebElement jobs = getWebDriver().findElement(By.id("jobs"));
        List<WebElement> rows = jobs.findElements(By.tagName("tr"));
        return rows.size();
    }

    /**
     *
     * @param job
     *            Set the job
     * @return the source job page
     */
    public final JobDetails selectJob(final int job) {
        WebElement jobs = getWebDriver().findElement(By.id("jobs"));
        List<WebElement> list = jobs.findElements(By.xpath("tr/td[1]/a"));
        return openAs(list.get(job - 1).getAttribute("href"),
                JobDetails.class);
    }
}
