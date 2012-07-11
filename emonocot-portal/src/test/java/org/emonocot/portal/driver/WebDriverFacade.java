package org.emonocot.portal.driver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.annotation.PreDestroy;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 *
 * @author ben
 *
 */
@Component
public class WebDriverFacade implements FactoryBean<WebDriver> {

    /**
     *
     * @return the webdriver
     * @throws IOException if there is a problem loading the
     *                     properties file
     */
    private WebDriver createWebDriver() throws IOException {
        Resource propertiesFile = new ClassPathResource(
                "META-INF/spring/application.properties");
        Properties properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        String webdriverMode = properties.getProperty("selenium.webdriver.mode", "local");
        String driverName = properties.getProperty("selenium.webdriver.impl", "org.openqa.selenium.firefox.FirefoxDriver");
        WebDriverBrowserType browser = WebDriverBrowserType.fromString(driverName);
		if (webdriverMode.equals("local")) {
			switch (browser) {
			case CHROME:
				String chromeLocation = properties
						.getProperty("selenium.webdriver.chromedriver.location");
				ChromeDriverService chromeService = new ChromeDriverService.Builder()
						.usingChromeDriverExecutable(new File(chromeLocation))
						.usingAnyFreePort().build();
				chromeService.start();
				return new RemoteWebDriver(chromeService.getUrl(),
						DesiredCapabilities.chrome());
			case INTERNET_EXPLORER:
				String 	internetExplorerLocation = properties
						.getProperty("selenium.webdriver.ie.location");
				InternetExplorerDriverService ieService = InternetExplorerDriverService.createDefaultService();
				ieService.start();
				return new RemoteWebDriver(ieService.getUrl(),
						DesiredCapabilities.internetExplorer());
			case FIREFOX:
			default:
				FirefoxBinary firefoxBinary = new FirefoxBinary();
				String display = properties.getProperty(
						"selenium.display.port", ":0");
				firefoxBinary.setEnvironmentProperty("DISPLAY", display);
				ProfilesIni allProfiles = new ProfilesIni();
				FirefoxProfile profile = allProfiles.getProfile("default");
				return new FirefoxDriver(firefoxBinary, profile);
			}
		} else {
			
			DesiredCapabilities capabilities = new DesiredCapabilities();			
			switch (browser) {
			case CHROME:
				capabilities = DesiredCapabilities.chrome();
				break;
			case INTERNET_EXPLORER:
				capabilities = DesiredCapabilities.internetExplorer();
				break;
			case FIREFOX:
			default:
				capabilities = DesiredCapabilities.firefox();
			}
			String platformName = properties.getProperty("selenium.webdriver.platformName", "LINUX");
	        WebDriverPlatformType platform = WebDriverPlatformType.valueOf(platformName);
			switch (platform) {
			case MAC:
				capabilities.setPlatform(Platform.MAC);
				break;
			case WINDOWS:
				capabilities.setPlatform(Platform.WINDOWS);
				break;
			case LINUX:
			default:
				capabilities.setPlatform(Platform.LINUX);
			}
			return new RemoteWebDriver(new URL("http://build.e-monocot.org:4444/wd/hub"), capabilities);
		}
    }

    /**
     *
     */
    private static WebDriver browser;

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

    /**
     * @return the object
     * @throws Exception an
     *             exception if there is a problem creating the object
     */
    public final WebDriver getObject() throws Exception {
        if (browser == null) {
            browser = createWebDriver();
        }
        return browser;
    }

    /**
     *
     * @return the web driver
     */
    public static WebDriver getWebDriver() {
        return browser;
    }

    /**
     * @return the type of object
     */
    public final Class<?> getObjectType() {
        return WebDriver.class;
    }

    /**
     * @return true if the object is a singleton
     */
    public final boolean isSingleton() {
        return false;
    }
}
