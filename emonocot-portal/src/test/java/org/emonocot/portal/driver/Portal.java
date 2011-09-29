package org.emonocot.portal.driver;

import java.io.IOException;
import java.util.Properties;

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
     * @throws IOException if there is a problem opening the properties file
     */
    public Portal() throws IOException {
        Resource propertiesFile = new ClassPathResource(
                "application.properties");
        Properties properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        setBaseUri(properties.getProperty("selenium.target",
                "http://build.e-monocot.org/latest/portal/"));
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
}
