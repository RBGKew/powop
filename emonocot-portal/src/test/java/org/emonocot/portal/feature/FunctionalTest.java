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
package org.emonocot.portal.feature;

import org.emonocot.test.Listener;
import org.emonocot.test.ListeningCucumber;
import org.emonocot.test.TakeScreenshotListener;
import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;



/**
 *
 * @author annapaola
 *
 */
@RunWith(ListeningCucumber.class)
@Listener(TakeScreenshotListener.class)
@Cucumber.Options(features = {
		"src/test/resources/features/About.feature",
		"src/test/resources/features/Authentication.feature",
		"src/test/resources/features/ClassificationTree.feature",
		"src/test/resources/features/FacetedSearch.feature",
		"src/test/resources/features/ImagePage.feature",
		"src/test/resources/features/ProjectPages.feature",
		"src/test/resources/features/SearchPage.feature",
		"src/test/resources/features/SourceAdminPage.feature",
		"src/test/resources/features/SourcePage.feature",
		"src/test/resources/features/SpatialSearch.feature",
		"src/test/resources/features/TaxonPage.feature",
		"src/test/resources/features/PhylogeneticTreePage.feature"
},
glue = "org.emonocot.portal.steps",
format = {"junit:target/surefire-reports/org.emonocot.portal.feature.FunctionalTest.xml", "json:target/cucumber.json", "pretty", "html:target/cucumber"})
public class FunctionalTest {

}
