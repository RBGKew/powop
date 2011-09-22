package org.emonocot.portal.driver;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PreDestroy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

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
     * @throws IOException if there is a problem loading the
     *                     properties file
     */
    private static WebDriver createWebDriver() throws IOException {
        Resource propertiesFile = new ClassPathResource(
                "application.properties");
        Properties properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        String driverName = properties.getProperty("selenium.webdriver.impl",
                "org.openqa.selenium.firefox.FirefoxDriver");
        WebDriverType type = WebDriverType.fromString(driverName);

        switch (type) {
        case FIREFOX:
        default:
            FirefoxBinary firefoxBinary = new FirefoxBinary();
            String display = properties.getProperty("selenium.display.port",
                    ":1");
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
     * @throws IOException
     *             If we cannot create the webdriver
     */
    public final WebDriver getWebDriver() throws IOException  {
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
