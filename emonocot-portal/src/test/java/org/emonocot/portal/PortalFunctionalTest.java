package org.emonocot.portal;


import org.junit.Test;


/**
 * 
 * @author ben
 * 
 */
public class PortalFunctionalTest extends AbstractSeleniumDriver {

    /**
     * Bug 36
     */
    @Test
    public final void testItalicName() {
        selenium.open("taxon/urn:kew.org:wcs:taxon:2295");
        selenium.selectWindow("null");
        verifyTrue(selenium.isTextPresent("Acorus"));
        assertEquals(
                "taxonName",
                selenium.getAttribute("css=article > div.row > div.twelvecol > header > h2 > span@class"));

    }

    /**
     * Test facetting on Type of object (Bug #72
     * http://129.67.24.160/bugzilla/show_bug.cgi?id=72)
     */
    @Test
    public final void testFacetByTypeTaxon() {
        selenium.open("search?q=&limit=10&start=0");
        assertTrue(selenium.isTextPresent("Image"));
        assertTrue(selenium.isTextPresent("Taxon"));
        selenium.click("link=*Taxon");
        selenium.waitForPageToLoad(defaultTimeOut);
        assertFalse(selenium.isTextPresent("Image"));
        assertTrue(selenium.isTextPresent("All Types"));
        selenium.click("link=All Types");
        selenium.waitForPageToLoad(defaultTimeOut);
        assertTrue(selenium.isTextPresent("Image"));
        assertTrue(selenium.isTextPresent("Taxon"));
    }

    /**
     * Test facetting on Type of object (Bug #72
     * http://129.67.24.160/bugzilla/show_bug.cgi?id=72)
     */
    @Test
    public final void testFacetByTypeImage() {
        selenium.open("search?q=&limit=10&start=0");
        assertTrue(selenium.isTextPresent("Image"));
        assertTrue(selenium.isTextPresent("Taxon"));
        selenium.click("link=*Image");
        selenium.waitForPageToLoad(defaultTimeOut);
        assertFalse(selenium.isTextPresent("Taxon"));
        assertTrue(selenium.isTextPresent("All Types"));
        selenium.click("link=All Types");
        selenium.waitForPageToLoad(defaultTimeOut);
        assertTrue(selenium.isTextPresent("Image"));
        assertTrue(selenium.isTextPresent("Taxon"));
    }
}
