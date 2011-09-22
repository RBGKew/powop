package org.emonocot.portal.driver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.PreDestroy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
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
     * @return the webdriver
     */
    private static WebDriver createWebDriver() {
        String driverName = System.getProperty("webdriver.impl",
                "org.openqa.selenium.firefox.FirefoxDriver");
        WebDriverType type = WebDriverType.fromString(driverName);

        switch(type) {
          case FIREFOX:
          default:
              FirefoxBinary firefoxBinary = new FirefoxBinary();
              String display = System.getProperty("DISPLAY", ":99");
              firefoxBinary.setEnvironmentProperty("DISPLAY", display);
              ProfilesIni allProfiles = new ProfilesIni();
              FirefoxProfile profile = allProfiles.getProfile("default");
              return new FirefoxDriver(firefoxBinary, profile);
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
            browser = createWebDriver();
        }
        return browser;
    }

    /**
     *
     */
    @PreDestroy
    public final void destroy() {
        if (browser != null) {
            browser.close();
            browser.quit();
            browser = null;
        }
    }
}
