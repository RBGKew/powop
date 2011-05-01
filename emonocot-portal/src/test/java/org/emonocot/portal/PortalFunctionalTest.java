package org.emonocot.portal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

/**
 *
 * @author ben
 *
 */
public class PortalFunctionalTest extends SeleneseTestCase {
    /**
     * The default port of the Selenium RC Server.
     */
    private static final int SELENIUM_SERVER_PORT = 4444;

    /**
     *
     */
    @Before
    public final void setUp() {
        selenium = new DefaultSelenium("localhost",
        PortalFunctionalTest.SELENIUM_SERVER_PORT,
        "*firefox /usr/lib/firefox-3.6/firefox",
        "http://129.67.24.160/latest/portal/");
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
     *
     */
    @After
    public final void tearDown() {
        selenium.stop();
    }
}
