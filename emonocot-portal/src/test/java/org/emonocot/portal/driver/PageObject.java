package org.emonocot.portal.driver;

import java.util.List;
import java.util.regex.Pattern;

import org.emonocot.test.TestDataManager;
import org.openqa.selenium.By;
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
 * @author ben
 */
public class PageObject {

    /**
    *
    */
    @FindBy(how = How.CLASS_NAME, using = "footer")
    private WebElement foot;

    /**
   *
   */
    @FindBy(how = How.CLASS_NAME, using = "navbar")
    private WebElement nav;
    /**
     *
     */
    private static final Integer AJAX_WAIT_STEP = 100;

    /**
     *
     */
    private static Logger logger = LoggerFactory.getLogger(PageObject.class);

    /**
     * @return the registration page
     */
    public final Register selectRegistrationLink() {
        return openAs(
                nav.findElement(By.linkText("Register")).getAttribute("href"),
                Register.class);
    }

    /**
     * @return the registration page
     */
    public final Identify selectIdentifyLink() {
        return openAs(
                nav.findElement(By.linkText("Identify")).getAttribute("href"),
                Identify.class);
    }

    /**
    *
    */
    @FindBy(how = How.TAG_NAME, using = "a")
    private List<WebElement> links;

    /**
     *
     */
    protected WebDriver webDriver;

    /**
    *
    */
    private String baseUri;

    /**
    *
    */
    protected TestDataManager testDataManager;

    /**
     * @param newBaseUri
     *            Set the base Uri
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
     * @param <T>
     *            The type of page
     * @param address
     *            the url of the page
     * @param pageClass
     *            the class of the page
     * @return the page
     */
    protected final <T extends PageObject> T openAs(final String address,
            final Class<T> pageClass) {
        open(address);
        Pattern loginPattern = Pattern.compile(".*/login.*");
        if (loginPattern.matcher(webDriver.getCurrentUrl()).matches()
                && !pageClass.equals(Login.class)) {
            Login loginPage = pageObjectInstance(Login.class);
            loginPage.setBaseUri(baseUri);
            loginPage.testDataManager = this.testDataManager;
            loginPage.webDriver = this.webDriver;
            throw new RequiresLoginException(loginPage);
        }
        return getPage(pageClass);
    }

    /**
     * @param <T>
     *            The type of page
     * @param pageClass
     *            the class of the page
     * @return the page
     */
    protected final <T extends PageObject> T getPage(final Class<T> pageClass) {
        T pageObject = pageObjectInstance(pageClass);
        pageObject.setBaseUri(baseUri);
        pageObject.testDataManager = this.testDataManager;
        pageObject.webDriver = this.webDriver;
        return pageObject;
    }

    /**
     * @param initialPause
     *            Set the initial wait time
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
     * @param <T>
     *            The type of page
     * @param pageClass
     *            Set the page class
     * @return an instance of class T
     */
    private <T extends PageObject> T pageObjectInstance(final Class<T> pageClass) {
        return PageFactory.initElements(webDriver, pageClass);
    }

    /**
     * @param address
     *            Set hte address
     */
    private void open(final String address) {
        webDriver.navigate().to(address);
    }

    /**
     * @return the web driver
     */
    public final WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * Assumption that we're handling authentication via tomcat and using
     * Cookies.
     */
    public final void disableAuthentication() {
        Cookie cookie = webDriver.manage().getCookieNamed("jsessionid");
        if (cookie != null) {
            webDriver.manage().deleteCookie(cookie);
        }
    }

    /**
     * @return the current (baseURI-relative including preceding slash) uri or
     *         null if the URI is malformed
     */
    public final String getUri() {
        try {
            java.net.URI uri = new java.net.URI(webDriver.getCurrentUrl());
            return uri.getPath();
        } catch (java.net.URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return the login page
     */
    public final Login selectLoginLink() {
        return this.openAs(
                nav.findElement(By.linkText("Login")).getAttribute("href"),
                Login.class);
    }

    /**
     * @return true, if the user is logged in
     */
    public final Boolean loggedIn() {
        WebElement logout = nav.findElement(By.linkText("Logout"));
        return (logout != null);
    }

    /**
     *
     */
    public final void logOut() {
        try {
            openAs(nav.findElement(By.linkText("Logout")).getAttribute("href"),
                    Index.class);
        } catch (Exception e) {
            // Nothing
        }
    }

    /**
     * @return the info message
     */
    public final String getInfoMessage() {
        WebElement webElement = webDriver.findElement(By
                .cssSelector(".alert.info p"));
        return webElement.getText();
    }

    /**
     * @param text
     *            Set the link text
     * @param clazz
     *            Set the expected page
     * @return the page object
     */
    public final PageObject selectLink(final String text,
            final Class<? extends PageObject> clazz) {
        return this.openAs(this.webDriver.findElement(By.linkText(text))
                .getAttribute("href"), clazz);
    }

    /**
     * @return the about page
     */
    public final About selectAboutLink() {
        return openAs(
                foot.findElement(By.linkText("About us")).getAttribute("href"),
                About.class);
    }

    /**
     * @return the contact page
     */
    public final Contact selectContactLink() {
        return openAs(
                foot.findElement(By.linkText("Contact us"))
                        .getAttribute("href"), Contact.class);
    }
}
