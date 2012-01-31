package org.emonocot.portal.driver;

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
public class SourceAdminPage extends PageObject {

    /**
     *
     */
    @FindBy(how = How.ID, using = "jobs")
    private WebElement jobs;
    /**
     *
     */
    @FindBy(how = How.ID, using = "source-uri")
    private WebElement uri;

    /**
     *
     */
   @FindBy(how = How.ID, using = "source-logo")
   private WebElement logo;

    /**
     *
     * @return the uri
     */
    public final String getSourceUri() {
        return uri.getAttribute("href");
    }



    /**
     *
     * @return the number of jobs listed
     */
    public final Integer getJobsListed() {
        List<WebElement> rows = jobs.findElements(By.tagName("tr"));
        return rows.size();
    }

    /**
     *
     * @param job Set the job
     * @return the source job page
     */
    public final SourceJobPage selectJob(final int job) {
        List<WebElement> list = jobs.findElements(By.xpath("tr/td[1]/a"));
        return openAs(
                list.get(job - 1).getAttribute("href"),
                SourceJobPage.class);
    }


    /**
     *
     * @return the source logo
     */
    public final String getSourceLogo() {
        return logo.getAttribute("src");
    }
}
