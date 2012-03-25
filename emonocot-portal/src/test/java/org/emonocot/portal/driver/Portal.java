package org.emonocot.portal.driver;

import java.io.IOException;
import java.util.Properties;


import org.emonocot.portal.driver.source.JobDetails;
import org.emonocot.test.TestDataManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 *
 * @author ben
 *
 */
@Component
public class Portal extends PageObject {

   /**
    *
    */
   private Logger logger = LoggerFactory
           .getLogger(Portal.class);

   /**
    * @param testDataManager Set the test data manager
    */
   @Autowired
   public final void setTestDataManager(final TestDataManager testDataManager) {
       super.testDataManager = testDataManager;
   }

   /**
    * @param webDriver set the webdriver
    */
   @Autowired
   public final void setWebDriver(final WebDriver webDriver) {
       super.webDriver = webDriver;
   }

    /**
     *
     * @throws IOException if there is a problem opening the properties file
     */
    public Portal() throws IOException {
        logger.debug("Portal() constructor");
        Resource propertiesFile = new ClassPathResource(
                "META-INF/spring/application.properties");
        Properties properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        String baseUri = properties.getProperty("functional.test.baseUri",
        "http://build.e-monocot.org");
        int port = Integer.valueOf(properties.getProperty(
                "functional.test.port", "80"));
        String basePath = properties.getProperty("functional.test.basePath",
                "/latest/portal");
        setBaseUri(baseUri + ":" + port + basePath + "/");
    }

    /**
     *
     * @return the home page
     */
    public final Index getHomePage() {
        return openAs(getBaseUri(), Index.class);
    }

    /**
     *
     * @param identifier The identifier of the taxon
     * @return a taxon page
     */
	public final PageObject getTaxonPage(final String identifier) {
		return openAs(getBaseUri() + "taxon/" + identifier,
				org.emonocot.portal.driver.taxon.Show.class);
	}

    /**
     *
     * @param query the query to execute
     * @return a search results page
     */
    public final Search search(final String query) {
        return openAs(getBaseUri() + "search?query=" + query,
                Search.class);
    }

    /**
     *
     * @param identifier Set the identifier of the image page
     * @return an image page
     */
    public final PageObject getImagePage(final String identifier) {
        return openAs(getBaseUri() + "image/" + identifier, 
        		org.emonocot.portal.driver.image.Show.class);
    }

    /**
     *
     * @param identifier Set the identifier of the source page
     * @return a source page
     */
    public final PageObject getSourcePage(final String identifier) {
        return openAs(getBaseUri() + "source/" + identifier, org.emonocot.portal.driver.source.Show.class);
    }

    /**
     *
     * @return the login page
     */
    public final Login getLoginPage() {
        return openAs(getBaseUri() + "login", Login.class);
    }
    
    /**
    *
    * @return the login page
    */
   public final About getAboutPage() {
       return openAs(getBaseUri() + "about", About.class);
   }

    /**
     *
     * @return the classification page
     */
    public final Classify getClassificationPage() {
        return openAs(getBaseUri() + "classify", Classify.class);
    }

    /**
     *
     * @return the group page
     */
    public final PageObject getListGroupsPage() {
        return openAs(getBaseUri() + "group", org.emonocot.portal.driver.group.List.class);
    }

    /**
     *
     * @param groupName Set the group name
     * @return the current page
     */
    public final PageObject getGroupPage(final String groupName) {
        return openAs(getBaseUri() + "group/" + groupName, org.emonocot.portal.driver.group.Show.class);
    }

    /**
     *
     * @param source Set the source
     * @param job Set the job
     * @return a source job page
     */
    public final JobDetails getSourceJobPage(final String source,
            final String job) {
        return openAs(getBaseUri() + "source/" + source + "/job/" + job,
                JobDetails.class);
    }

    /**
     *
     * @return the source admin page
     */
    public final PageObject getSourceAdminPage() {
        return openAs(getBaseUri() + "admin/source",
                org.emonocot.portal.driver.admin.source.List.class);
    }

    /**
     *
     * @param source Set the source
     * @return t
     */
	public final PageObject getSourceAdminPage(final String source) {
		return openAs(getBaseUri() + "admin/source/" + source,
				org.emonocot.portal.driver.admin.source.Show.class);
	}
}
