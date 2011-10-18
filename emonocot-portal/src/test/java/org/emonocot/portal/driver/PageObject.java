package org.emonocot.portal.driver;

import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
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
