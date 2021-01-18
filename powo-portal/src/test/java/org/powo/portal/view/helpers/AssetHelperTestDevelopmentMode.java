package org.powo.portal.view.helpers;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.Test;

import com.github.jknack.handlebars.Handlebars;

public class AssetHelperTestDevelopmentMode extends AbstractHelperTest {

	@Override
	protected Handlebars newHandlebars() {
		Handlebars handlebars = super.newHandlebars();
		handlebars.registerHelpers(
				new AssetHelper(true, Clock.fixed(Instant.parse("2020-01-18T17:26:00Z"), ZoneOffset.UTC)));
		return handlebars;
	}

	@Test
	public void testJsAsset() throws IOException {
		shouldCompileTo("{{{asset \"js/all.min.js\"}}}", this, "js/all.min.js?t=1579368360000");
	}

	@Test
	public void testCssAsset() throws IOException {
		shouldCompileTo("{{{asset \"css/style.min.css\"}}}", this, "css/style.min.css?t=1579368360000");
	}
}
