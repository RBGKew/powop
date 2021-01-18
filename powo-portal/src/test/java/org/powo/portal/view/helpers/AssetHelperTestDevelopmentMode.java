package org.powo.portal.view.helpers;

import java.io.IOException;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.jknack.handlebars.Handlebars;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/applicationContext.xml" })
@TestPropertySource(properties = { "development.mode=true" })
public class AssetHelperTestDevelopmentMode extends AbstractHelperTest {

	@Autowired
	private AssetHelper assetHelper;

	@Override
	protected Handlebars newHandlebars() {
		Handlebars handlebars = super.newHandlebars();
		handlebars.registerHelpers(assetHelper);
		return handlebars;
	}

	@Test
	public void testJsAsset() throws IOException, InterruptedException {
		assertAssetHasTimestamp("{{{asset \"js/all.min.js\"}}}", "js/all.min.js");
	}

	@Test
	public void testCssAsset() throws IOException, InterruptedException {
		assertAssetHasTimestamp("{{{asset \"css/style.min.js\"}}}", "css/style.min.js");
	}

	private void assertAssetHasTimestamp(String template, String asset) throws IOException, InterruptedException {
		long testStartMillis = System.currentTimeMillis();
		Thread.sleep(1); // Force template timestamp to be after testStartMillis

		String result = renderTemplate(template, new Hash());
		String pattern = asset.replaceAll("\\.", "\\\\.") + "\\?t=[0-9]+";
		Assert.assertTrue("Compiled asset path should include t query param", Pattern.matches(pattern, result));

		String[] pathParts = result.split("\\?");
		String assetPath = pathParts[0];
		Assert.assertEquals("Asset path should be " + asset, asset, assetPath);

		String queryString = pathParts[1];
		String[] queryParts = queryString.split("=");
		String queryParam = queryParts[0];
		Assert.assertEquals("Query param should be t", "t", queryParam);

		long timestamp = Long.parseLong(queryParts[1]);
		Thread.sleep(1); // Force template timestamp to be before testEndMillis
		long testEndMillis = System.currentTimeMillis();
		Assert.assertTrue(
				"Timestamp should be chosen at runtime, so after test start time but before this assertion time",
				testStartMillis < timestamp && timestamp < testEndMillis);
	}
}
