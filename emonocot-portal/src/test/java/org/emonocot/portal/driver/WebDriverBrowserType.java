/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
	CHROME("org.openqa.selenium.chrome.ChromeDriver"), 
    FIREFOX("org.openqa.selenium.firefox.FirefoxDriver"),
    SAFARI("org.openqa.selenium.safari.SafariDriver"), 
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
