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
		//"src/test/resources/features/About.feature",
		//"src/test/resources/features/Authentication.feature",
//		"src/test/resources/features/ClassificationTree.feature",
//		"src/test/resources/features/FacetedSearch.feature",
//		"src/test/resources/features/ImagePage.feature",
//		"src/test/resources/features/ProjectPages.feature",
//		"src/test/resources/features/SearchPage.feature",
//		"src/test/resources/features/SourceAdminPage.feature",
//		"src/test/resources/features/SourcePage.feature",
//		"src/test/resources/features/SpatialSearch.feature",
		"src/test/resources/features/TaxonPage.feature"
//		,
//		"src/test/resources/features/PhylogeneticTreePage.feature"
		},
		          glue = "org.emonocot.portal.steps",
		          format = {"junit:target/surefire-reports/org.emonocot.portal.feature.FunctionalTest.xml", "json:target/cucumber.json", "pretty", "html:target/cucumber"})
public class FunctionalTest {

}
