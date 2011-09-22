package org.emonocot.portal.driver;

/**
 *
 * @author ben
 *
 */
public enum WebDriverType {
    /**
     *
     */
    FIREFOX("org.openqa.selenium.firefox.FirefoxDriver");

    /**
     *
     */
    private String className;

    /**
     *
     * @param newClassName set the classname
     */
    private WebDriverType(final String newClassName) {
        this.className = newClassName;
    }

    /**
     *
     * @param className the name of the web driver class
     * @return a web driver type
     */
    public static WebDriverType fromString(final String className) {
        for (WebDriverType t : WebDriverType.values()) {
            if (t.className == className) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unrecognized web driver class + "
                + className);
    }

}
