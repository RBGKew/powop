package org.emonocot.portal.driver;

import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
public abstract class PageObject {

    /**
     *
     */
    private static final Integer AJAX_WAIT_STEP = 100;

    /**
     *
     */
    private static Logger logger = LoggerFactory.getLogger(PageObject.class);

   /**
    *
    */
   @FindBy(how = How.TAG_NAME, using = "a")
   private List<WebElement> links;

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
   protected TestDataManager testDataManager;

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
        Pattern loginPattern = Pattern.compile(".*/login.*");
        if (loginPattern.matcher(webDriver.getCurrentUrl()).matches()) {
            LoginPage loginPage = pageObjectInstance(LoginPage.class);
            loginPage.setBaseUri(baseUri);
            loginPage.testDataManager = this.testDataManager;
            throw new RequiresLoginException(loginPage);
        }
        T pageObject = pageObjectInstance(pageClass);
        pageObject.setBaseUri(baseUri);
        pageObject.testDataManager = this.testDataManager;
        return pageObject;
    }

    /**
     * @param initialPause Set the initial wait time
     */
    public final void waitForAjax(final Integer initialPause) {
        try {
            Thread.sleep(initialPause);
            while (true) {
                Boolean ajaxIsComplete = (Boolean) ((JavascriptExecutor) webDriver)
                        .executeScript("return jQuery.active == 0");
                if (ajaxIsComplete) {
                    break;
                }
                Thread.sleep(AJAX_WAIT_STEP);
            }
        } catch (InterruptedException ie) {
            logger.error(ie.getMessage());
        }
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

    /**
     *
     * @return the web driver
     */
    public final WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * Assumption that we're handling authentication via
     * tomcat and using Cookies.
     */
    public final void disableAuthentication() {
        Cookie cookie = webDriver.manage().getCookieNamed("jsessionid");
        if (cookie != null) {
            webDriver.manage().deleteCookie(cookie);
        }
    }
}
