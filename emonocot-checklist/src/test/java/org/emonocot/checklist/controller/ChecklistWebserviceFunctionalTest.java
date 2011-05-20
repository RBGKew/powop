package org.emonocot.checklist.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.SeleneseTestCase;

/**
 *
 * @author ben
 *
 */
public class ChecklistWebserviceFunctionalTest extends SeleneseTestCase {

    /**
     *
     */
    private HttpCommandProcessor httpCommandProcessor;

    /**
     * The default port of the Selenium RC Server.
     */
    private static final int SELENIUM_SERVER_PORT = 4444;

    /**
     * @throws Exception
     *             if there is a problem instantiating the selenium server
     */
    @Before
    public final void setUp() throws Exception {
        httpCommandProcessor = new HttpCommandProcessor("localhost",
                ChecklistWebserviceFunctionalTest.SELENIUM_SERVER_PORT,
                System.getProperty("selenium.browser.profile",
                        "*firefox /usr/lib/firefox-3.6/firefox"),
                System.getProperty("selenium.target.checklist",
                        "http://129.67.24.160/latest/checklist"));
        selenium = new DefaultSelenium(httpCommandProcessor);
        selenium.start();
    }

    /**
     *
     * @throws Exception
     *             if there is a problem with the test
     */
    @Test
    public final void testPing() throws Exception {
        selenium.open("./endpoint");
        assertTrue(httpCommandProcessor
                .getBoolean(
                        "isTextPresentXML",
                        new String[] {"",
                                "<results xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"></results>" }));
    }

    /**
     * @throws Exception
     *             if there is a problem tearing down the test
     */
    @After
    public final void tearDown() throws Exception {
        selenium.stop();
    }

}
