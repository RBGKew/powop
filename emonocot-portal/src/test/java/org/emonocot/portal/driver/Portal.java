package org.emonocot.portal.driver;

import java.io.IOException;
import java.util.Properties;

import org.emonocot.portal.driver.organisation.ResourceList;
import org.emonocot.portal.driver.organisation.ResourceOutput;
import org.emonocot.portal.remoting.IdentificationKeyDaoImpl;
import org.emonocot.portal.remoting.ImageDaoImpl;
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
    *
    * @param imageDao set the image dao
    */
   @Autowired
   public final void setImageDaoImpl(ImageDaoImpl imageDao) {
	   this.imageDao = imageDao;
   }
   
   /**
   *
   * @param imageDao set the image dao
   */
  @Autowired
  public final void setIdentificationKeyDaoImpl(IdentificationKeyDaoImpl keyDao) {
	   this.keyDao = keyDao;
  }

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
    * @return the feature page
    */
   public final Index getFeaturePage() {
       return openAs(getBaseUri() + "tour", Index.class);
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
    * @param x1 the first latitude
    * @param y1 the first longitude
    * @param x2 the second latitude
    * @param y2 the second longitude
    * @return a spatial search results page
    */
   public final SpatialSearch spatialSearch(final Float x1, final Float y1, final Float x2, final Float y2) {
	   if(x1 == null || y1 == null || x2 == null || y2 == null) {
		   return openAs(getBaseUri() + "spatial",
	               SpatialSearch.class);
	   } else {
		   return openAs(getBaseUri() + "spatial?x1=" + x1 + "&y1=" + y1 + "&x2=" + x2 + "&y2=" + y2,
	               SpatialSearch.class);
	   }
       
   }

    /**
     *
     * @param identifier Set the identifier of the image page
     * @return an image page
     */
    public final PageObject getImagePage(final String identifier) {
        return openAs(imageDao.getPageLocation(identifier), 
        		org.emonocot.portal.driver.image.Show.class);
    }

    /**
     *
     * @param identifier Set the identifier of the source page
     * @return a source page
     */
    public final PageObject getSourcePage(final String identifier) {
        return openAs(getBaseUri() + "organisation/" + identifier, org.emonocot.portal.driver.organisation.Show.class);
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
    * @return the about page
    */
   public final About getAboutPage() {
       return openAs(getBaseUri() + "about", About.class);
   }

    /**
     *
     * @return the classification page
     */
    public final Classification getClassificationPage() {
        return openAs(getBaseUri() + "classification", Classification.class);
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
   public final ResourceList getSourceJobsPage(final String source) {
       return openAs(getBaseUri() + "organisation/" + source + "/resource", ResourceList.class);
   }

    /**
     *
     * @param source Set the source
     * @param job Set the job
     * @return a source job page
     */
    public final ResourceOutput getSourceJobPage(final String source,
            final String job) {
        return openAs(getBaseUri() + "organisation/" + source + "/resource/" + job,
                ResourceOutput.class);
    }

    /**
     *
     * @return the source admin page
     */
    public final PageObject getSourceListPage() {
        return openAs(getBaseUri() + "organisation",
                org.emonocot.portal.driver.organisation.List.class);
    }

    /**
     * @return the classify page
     */
    public PageObject getClassifyPage() {
        return openAs(getBaseUri() + "classify", Classify.class);
    }
}
