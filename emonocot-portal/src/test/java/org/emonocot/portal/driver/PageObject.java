package org.emonocot.portal.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 *
 * @author ben
 *
 */
public abstract class PageObject {

    /**
     *
     */
    private WebDriver webDriver;

   /**
    *
    */
   private String baseUri;

    /**
     *
     */
    public PageObject() {
        tryCreateWebDriver();
    }

    /**
     *
     * @param newBaseUri Set the base Uri
     */
    public final void setBaseUri(final String newBaseUri) {
        this.baseUri = newBaseUri;
    }

    /**
     * @return the base uri
     */
    public final String getBaseUri() {
        return baseUri;
    }

    /**
     *
     * @param <T>
     * @param address
     * @param pageClass
     * @return
     */
    protected <T extends PageObject> T openAs(String address, Class<T> pageClass) {
        open(address);
        T pageObject = pageObjectInstance(pageClass);
        pageObject.setBaseUri(baseUri);
        return pageObject;
    }

    /**
     *
     * @param <T>
     * @param pageClass
     * @return
     */
    private <T extends PageObject> T pageObjectInstance(Class<T> pageClass) {
        return PageFactory.initElements(webDriver, pageClass);
    }

    /**
     *
     * @param address
     */
    private void open(String address) {
        webDriver.navigate().to(address);
    }

    /**
     *
     */
    private void tryCreateWebDriver() {
        try {
            webDriver = new WebDriverFacade().getWebDriver();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
