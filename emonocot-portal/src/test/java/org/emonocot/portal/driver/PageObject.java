package org.emonocot.portal.driver;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.emonocot.portal.remoting.IdentificationKeyDaoImpl;
import org.emonocot.portal.remoting.ImageDaoImpl;
import org.emonocot.test.TestDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
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
	
	private Pattern pattern = Pattern.compile("^(.*?);jsessionid=[A-Z0-9]+$");

    /**
    *
    */
    @FindBy(how = How.TAG_NAME, using = "footer")
    private WebElement foot;
    
    /**
    *
    */
    @FindBy(how = How.ID_OR_NAME, using = "identify-box")
    private WebElement identifyBox;
    
    /**
    *
    */
    @FindBy(how = How.ID_OR_NAME, using = "classify-box")
    private WebElement classifyBox;

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
     * @return the identify link
     */
    public final Identify selectIdentifyLink() {
        return openAs(
        		identifyBox.findElement(By.linkText("identification features")).getAttribute("href"),
                Identify.class);
    }
    
    /**
     * @return the identify link
     */
    public final Identify selectClassifyLink() {
        return openAs(
        		classifyBox.findElement(By.linkText("classification features")).getAttribute("href"),
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
    protected ImageDaoImpl imageDao;
    
    /**
    *
    */
    protected IdentificationKeyDaoImpl keyDao;

    /**
    *
    */
    private String baseUri;
    
    private String host;
    
    private String basePath;
    
    private int port;

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
            loginPage.imageDao = this.imageDao;
            loginPage.keyDao = this.keyDao;
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
        pageObject.setHost(host);
        pageObject.setPort(port);
        pageObject.setBasePath(basePath);
        pageObject.testDataManager = this.testDataManager;
        pageObject.webDriver = this.webDriver;
        pageObject.imageDao = this.imageDao;
        pageObject.keyDao = this.keyDao;
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
            String basePath = new java.net.URI(getBaseUri()).getPath();
            String relPath = uri.getPath().replace(basePath, "");
            relPath = relPath.startsWith("/") ? relPath.substring(1) : relPath;
            Matcher matcher = pattern.matcher(relPath);
            if(matcher.matches()) {
            	relPath = matcher.group(1);
            }
            return relPath;
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
                nav.findElement(By.linkText("Sign in")).getAttribute("href"),
                Login.class);
    }

    /**
     * @return true, if the user is logged in
     */
    public final Boolean loggedIn() {
    	WebElement userMenu = nav.findElement(By.cssSelector("#menuUser"));
        return (userMenu != null);
    }

    /**
     *
     */
    public final void logOut() {
        try {
            openAs(getBaseUri() + "/logout", Index.class);
        } catch (Exception e) {
           System.out.println(e.getMessage());
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
    
    public final TermsOfUse selectTermsOfUseLink() {
        return openAs(
                foot.findElement(By.linkText("Terms of Use")).getAttribute("href"),
                TermsOfUse.class);
    }

    /**
     * @return the contact page
     */
    public final Contact selectContactLink() {
        return openAs(
                foot.findElement(By.linkText("Contact us"))
                        .getAttribute("href"), Contact.class);
    }

    /**
     * @param text partial text of link to search for
     * @return false if there is a NoSuchElementException is thrown
     *  true otherwise
     */
    public final boolean isLinkPresent(String text) {
        try {
            webDriver.findElement(By.partialLinkText(text));
            // if an element is found
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * @param text
     *            the text of the link
     * @return the target of the link if it is found on this page
     */
    public PageObject selectLink(String text) {
        return selectLink(text, PageObject.class);
    }

	/**
	 * @param elementId
	 * @return Whether an element with this id is visible
	 */
	public boolean isElementVisible(String elementId) {
		try {
			 WebElement element = webDriver.findElement(By.id(elementId));
			return element.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

    /**
     * @param paragraphNameOrId
     * @return
     */
    public String paragraphText(String paragraphNameOrId) {
        WebElement element = null;
        try{
            logger.info("Attempting to find element by id:" + paragraphNameOrId);
            element = webDriver.findElement(By.id(paragraphNameOrId));
        } catch (NoSuchElementException e0) {
            logger.info("No element with id:" + paragraphNameOrId + ". Attempting to find element with this name");
            try {
                element = webDriver.findElement(By.name(paragraphNameOrId));
            } catch (NoSuchElementException e1) {
                logger.warn("Unable to find element with either id or name:" + paragraphNameOrId, e1);
                element = null;
            }
        }
        if(element == null) {
            return null;
        } else {
            return element.getText();
        }
    }

    /**
     * @return The source of the page.  Dependant on the implementing class, this
     * can be the source when the page was retrieved, the page as modified by script
     * or some other representation of the DOM 
     */
    public String getText() {
        return webDriver.getPageSource();
    }

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
    
}
