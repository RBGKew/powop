package org.emonocot.portal.remoting;

import java.io.IOException;
import java.util.Properties;

import org.emonocot.model.description.Distribution;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.geography.Country;
import org.emonocot.model.media.Image;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.user.User;
import org.emonocot.persistence.dao.ImageDao;
import org.emonocot.persistence.dao.TaxonDao;
import org.emonocot.portal.feature.TestAuthentication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class RestApiFunctionalTest {

    /**
     *
     */
    @Autowired
    private TaxonDao taxonDao;

   /**
    *
    */
   @Autowired
   private ImageDao imageDao;

   /**
    *
    */
   private String password;

   /**
    *
    */
   private String username;

   /**
    *
    * @throws IOException if there is a problem reading the properties file
    */
    public RestApiFunctionalTest() throws IOException {
        Resource propertiesFile = new ClassPathResource(
                "application.properties");
        Properties properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        username = properties.getProperty("functional.test.username", null);
        password = properties.getProperty("functional.test.password", null);
    }

    /**
     *
     */
    @Before
    public final void setUp() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        securityContext
          .setAuthentication(
                  new TestAuthentication(user));
    }

    /**
     *
     */
    @After
    public final void tearDown() {
        SecurityContextHolder.clearContext();
    }

  /**
   *
   */
  @Test
  public final void testImage() {
      Image image = new Image();
      image.setCaption("Acorus");
      image.setIdentifier("urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg");
      image.setUrl("http://upload.wikimedia.org/wikipedia/commons/2/25/Illustration_Acorus_calamus0.jpg");
      imageDao.save(image);
      imageDao.delete("urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg");
  }

    /**
     *
     */
    @Test
    public final void testTaxon() {
        Image image = new Image();
        image.setCaption("Acorus");
        image.setIdentifier("urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg");
        image.setUrl("http://upload.wikimedia.org/wikipedia/commons/2/25/Illustration_Acorus_calamus0.jpg");
        imageDao.save(image);

        Taxon taxon = new Taxon();
        taxon.setName("Acorus");
        taxon.setIdentifier("urn:kew.org:wcs:taxon:2295");
        TextContent textContent = new TextContent();
        textContent.setContent("Lorem ipsum");
        textContent.setFeature(Feature.habitat);
        textContent.setTaxon(taxon);
        taxon.getContent().put(Feature.habitat, textContent);
        Distribution distribution = new Distribution();
        distribution.setTaxon(taxon);
        distribution.setRegion(Country.REU);
        taxon.getDistribution().put(Country.REU, distribution);
        taxon.getImages().add(image);
        taxonDao.save(taxon);

        taxonDao.delete("urn:kew.org:wcs:taxon:2295");
        imageDao.delete("urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg");
    }
}
