package org.emonocot.portal;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

public class AbstractSeleniumDriver extends SeleneseTestCase {

    /**
    *
    */
    private Properties properties;
    /**
     * A reasonable timeout for clicking links - 10 seconds even for UAT
     */
    protected static final String defaultTimeOut = "10000";
    /**
     * The default port of the Selenium RC Server.
     */
    private static final int SELENIUM_SERVER_PORT = 4444;

    public AbstractSeleniumDriver() {
        super();
    }

    public AbstractSeleniumDriver(String name) {
        super(name);
    }

    /**
     *
     */
    @Before
    public final void setUp() throws Exception {
        Resource propertiesFile = new ClassPathResource(
                "application.properties");
        properties = new Properties();
        properties.load(propertiesFile.getInputStream());
    
        selenium = new DefaultSelenium("localhost",
                AbstractSeleniumDriver.SELENIUM_SERVER_PORT,
                properties.getProperty("selenium.browser.profile",
                        "*firefox /usr/lib/firefox-3.6/firefox"),
                properties.getProperty("selenium.target.portal",
                        "http://build.e-monocot.org/latest/portal"));
        selenium.start();
    }

    /**
     *
     */
    @After
    public final void tearDown() {
        selenium.stop();
    }

    public void getPage(String string) {
        selenium.open(string);
    }

    public String getPageTitle() {
        return selenium.getText("css=article > div.row > div.twelvecol > header > h2 > span");
    }

    public String getPageTitleClass() {
        return selenium.getAttribute("css=article > div.row > div.twelvecol > header > h2 > span@class");
    }

    public void addTaxon(String taxonId, String name) {
        // TODO Auto-generated method stub
        
    }

}