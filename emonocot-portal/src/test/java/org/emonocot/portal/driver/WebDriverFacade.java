package org.emonocot.portal.driver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import cucumber.annotation.After;

/**
 *
 * @author ben
 *
 */
@Component
public class WebDriverFacade {

    /**
     *
     */
    private static Constructor<WebDriver> driverConstructor = getDriverConstructor();

    /**
     *
     * @return the constructor for the web dri
     */
    private static Constructor<WebDriver> getDriverConstructor() {
        String driverName = System.getProperty("webdriver.impl",
                "org.openqa.selenium.firefox.FirefoxDriver");
        try {
            return (Constructor<WebDriver>) Thread.currentThread()
                    .getContextClassLoader().loadClass(driverName)
                    .getConstructor();
        } catch (Throwable problem) {
            problem.printStackTrace();
            throw new RuntimeException("Couldn't load " + driverName, problem);
        }
    }

    /**
     *
     */
    private static WebDriver browser;

    /**
     *
     * @return the web driver
     * @throws InvocationTargetException
     *             If we cannot invoke the constructor
     * @throws IllegalAccessException
     *             If we are not allowed to access the constructor
     * @throws InstantiationException
     *             If we cannot instantiate the constructor
     */
    public final WebDriver getWebDriver() throws InvocationTargetException,
            IllegalAccessException, InstantiationException {
        if (browser == null) {
            browser = driverConstructor.newInstance();
        }
        return browser;
    }

    /**
     *
     */
    @After
    public final void destroy() {
        if (browser != null) {
            browser.close();
            browser.quit();
            browser = null;
        }
    }
}
