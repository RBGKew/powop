package org.emonocot.portal;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

/**
 *
 * @author ben
 *
 */
public class PortalFunctionalTest extends SeleneseTestCase {
   /**
    *
    */
   private Properties properties;
    /**
     * The default port of the Selenium RC Server.
     */
    private static final int SELENIUM_SERVER_PORT = 4444;

    /**
     *
     */
    @Before
    public final void setUp()  throws Exception {
    	Resource propertiesFile
        = new ClassPathResource("application.properties");
        properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        
        selenium = new DefaultSelenium("localhost",
        PortalFunctionalTest.SELENIUM_SERVER_PORT,
        properties.getProperty("selenium.browser.profile","*firefox /usr/lib/firefox-3.6/firefox"),
        properties.getProperty("selenium.target.portal","http://build.e-monocot.org/latest/portal")
        );
        selenium.start();
    }

    /**
     *
     */
    @Test
    public final void testSimple() {
        assertTrue(selenium.isTextPresent(""));
    }
    
    /**
     *Bug 36
     */
    @Test
    public final void testItalicName() {
    	selenium.open("taxon/urn:kew.org:wcs:taxon:2295");
    	selenium.selectWindow("null");
    	verifyTrue(selenium.isTextPresent("Acorus"));
    	assertEquals("taxonName", selenium.getAttribute("css=article > div.row > div.twelvecol > header > h2 > span@class"));

    }

    /**
     *
     */
    @After
    public final void tearDown() {
        selenium.stop();
    }
}
