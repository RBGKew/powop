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

import java.io.IOException;
import java.util.Properties;

import org.emonocot.portal.remoting.IdentificationKeyDaoImpl;
import org.emonocot.portal.remoting.ImageDaoImpl;
import org.emonocot.test.TestDataManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class Portal extends PageObject {

	private Logger logger = LoggerFactory.getLogger(Portal.class);

	/**
	 *
	 * @param imageDao set the image dao
	 */
	@Autowired
	public final void setImageDaoImpl(ImageDaoImpl imageDao) {
		this.imageDao = imageDao;
	}

	/**
	 *
	 * @param imageDao set the image dao
	 */
	@Autowired
	public final void setIdentificationKeyDaoImpl(IdentificationKeyDaoImpl keyDao) {
		this.keyDao = keyDao;
	}

	/**
	 * @param testDataManager Set the test data manager
	 */
	@Autowired
	public final void setTestDataManager(final TestDataManager testDataManager) {
		super.testDataManager = testDataManager;
	}

	/**
	 * @param webDriver set the webdriver
	 */
	@Autowired
	public final void setWebDriver(final WebDriver webDriver) {
		super.webDriver = webDriver;
	}

	/**
	 *
	 * @throws IOException if there is a problem opening the properties file
	 */
	public Portal() throws IOException {
		logger.debug("Portal() constructor");
		Resource propertiesFile = new ClassPathResource("META-INF/spring/application.properties");
		Properties properties = new Properties();
		properties.load(propertiesFile.getInputStream());
		String host = properties.getProperty("functional.test.baseUri", "http://build.e-monocot.org");
		int port = Integer.valueOf(properties.getProperty("functional.test.port", "80"));
		String basePath = properties.getProperty("functional.test.basePath", "/latest/portal");
		setHost(host);
		setPort(port);
		setBasePath(basePath);
		setBaseUri(host + ":" + port + basePath );
	}

	public final Index getHomePage() {
		return openAs(getBaseUri(), Index.class);
	}

	public final Index getFeaturePage() {
		return openAs(getBaseUri() + "/tour", Index.class);
	}

	public final PageObject getTaxonPage(final String identifier) {
		return openAs(getBaseUri() + "/taxon/" + identifier,
				org.emonocot.portal.driver.taxon.Show.class);
	}

	public final Search search(final String query) {
		return openAs(getBaseUri() + "/search?query=" + query, Search.class);
	}

	/**
	 *
	 * @param x1 the first latitude
	 * @param y1 the first longitude
	 * @param x2 the second latitude
	 * @param y2 the second longitude
	 * @return a spatial search results page
	 */
	public final SpatialSearch spatialSearch(final Float x1, final Float y1, final Float x2, final Float y2) {
		if(x1 == null || y1 == null || x2 == null || y2 == null) {
			return openAs(getBaseUri() + "/spatial", SpatialSearch.class);
		} else {
			return openAs(getBaseUri() + "/spatial?x1=" + x1 + "&y1=" + y1 + "&x2=" + x2 + "&y2=" + y2, SpatialSearch.class);
		}
	}

	public final PageObject getImagePage(final String identifier) {
		return openAs(getBaseUri() + "/image/" + identifier, org.emonocot.portal.driver.image.Show.class);
	}

	public final PageObject getSourcePage(final String identifier) {
		return openAs(getBaseUri() + "/organisation/" + identifier, org.emonocot.portal.driver.organisation.Show.class);
	}

	public final Login getLoginPage() {
		return openAs(getBaseUri() + "/login", Login.class);
	}

	public final About getAboutPage() {
		return openAs(getBaseUri() + "/about", About.class);
	}

	public final Classification getClassificationPage() {
		return openAs(getBaseUri() + "/classification", Classification.class);
	}

	public final PageObject getListGroupsPage() {
		return openAs(getBaseUri() + "/group", org.emonocot.portal.driver.group.List.class);
	}

	public final PageObject getGroupPage(final String groupName) {
		return openAs(getBaseUri() + "/group/" + groupName, org.emonocot.portal.driver.group.Show.class);
	}

	public final PageObject getSourceListPage() {
		return openAs(getBaseUri() + "/organisation", org.emonocot.portal.driver.organisation.List.class);
	}

	public PageObject getClassifyPage() {
		return openAs(getBaseUri() + "/classify", Classify.class);
	}

	public PageObject getUpdateSourcePage(String source) {
		return openAs(getBaseUri() + "/organisation/" + source + "?form=true", org.emonocot.portal.driver.organisation.Update.class);
	}
}
