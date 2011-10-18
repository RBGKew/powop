package org.emonocot.portal.driver;

import java.io.IOException;
import java.util.Properties;

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
    * @param testDataManager set the test data manager
    */
    @Autowired
    public void setTestDataManager(TestDataManager testDataManager) {
        super.testDataManager = testDataManager;
    }

    /**
     *
     * @throws IOException if there is a problem opening the properties file
     */
    public Portal() throws IOException {
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
     * @param source the name of the source
     * @return the source admin page
     */
    public final SourceAdminPage getSourceAdminPage(final String source) {
        return openAs(getBaseUri() + "admin/source/" + source,
                SourceAdminPage.class);
    }
}
