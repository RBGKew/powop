package org.emonocot.portal.driver;

/**
 *
 * @author ben
 *
 */
public enum WebDriverBrowserType {
    /**
     *
     */
    FIREFOX("org.openqa.selenium.firefox.FirefoxDriver"),
    CHROME("org.openqa.selenium.chrome.ChromeDriver"), 
    INTERNET_EXPLORER("org.openqa.selenium.ie.InternetExplorerDriver");

    /**
     *
     */
    private String className;

    /**
     *
     * @param newClassName set the classname
     */
    private WebDriverBrowserType(final String newClassName) {
        this.className = newClassName;
    }

    /**
     *
     * @param className the name of the web driver class
     * @return a web driver type
     */
    public static WebDriverBrowserType fromString(final String className) {
        for (WebDriverBrowserType t : WebDriverBrowserType.values()) {
            if (t.className.equals(className)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unrecognized web driver class "
                + className);
    }

}
