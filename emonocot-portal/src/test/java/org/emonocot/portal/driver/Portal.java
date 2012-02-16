package org.emonocot.portal.driver;

import java.io.IOException;
import java.util.Properties;

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
                "application.properties");
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
    public final HomePage getHomePage() {
        return openAs(getBaseUri(), HomePage.class);
    }

    /**
     *
     * @param identifier The identifier of the taxon
     * @return a taxon page
     */
    public final TaxonPage getTaxonPage(final String identifier) {
        return openAs(getBaseUri() + "taxon/" + identifier, TaxonPage.class);
    }

    /**
     *
     * @param query the query to execute
     * @return a search results page
     */
    public final SearchResultsPage search(final String query) {
        return openAs(getBaseUri() + "search?query=" + query,
                SearchResultsPage.class);
    }

    /**
     *
     * @param identifier Set the identifier of the image page
     * @return an image page
     */
    public final ImagePage getImagePage(final String identifier) {
        return openAs(getBaseUri() + "image/" + identifier, ImagePage.class);
    }

    /**
     *
     * @param identifier Set the identifier of the source page
     * @return a source page
     */
    public final SourcePage getSourcePage(final String identifier) {
        return openAs(getBaseUri() + "source/" + identifier, SourcePage.class);
    }

    /**
     *
     * @return the login page
     */
    public final LoginPage getLoginPage() {
        return openAs(getBaseUri() + "login", LoginPage.class);
    }

    /**
     *
     * @return the classification page
     */
    public final ClassificationPage getClassificationPage() {
        return openAs(getBaseUri() + "classify", ClassificationPage.class);
    }

    /**
     *
     * @return the group page
     */
    public final PageObject getGroupPage() {
        return openAs(getBaseUri() + "group", GroupPage.class);
    }

    /**
     *
     * @param groupName Set the group name
     * @return the current page
     */
    public final PageObject getGroupPage(final String groupName) {
        return openAs(getBaseUri() + "group/" + groupName, GroupPage.class);
    }

    /**
     *
     * @param source Set the source
     * @param job Set the job
     * @return a source job page
     */
    public final SourceJobPage getSourceJobPage(final String source,
            final String job) {
        return openAs(getBaseUri() + "source/" + source + "/job/" + job,
                SourceJobPage.class);
    }
}
