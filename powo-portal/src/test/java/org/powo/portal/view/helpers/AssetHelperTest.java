package org.powo.portal.view.helpers;

import java.io.IOException;

import org.junit.Test;

import com.github.jknack.handlebars.Handlebars;

public class AssetHelperTest extends AbstractHelperTest {

	@Override
	protected Handlebars newHandlebars() {
		Handlebars handlebars = super.newHandlebars();
		handlebars.registerHelpers(new AssetHelper());
		return handlebars;
	}

	@Test
	public void testJsAsset() throws IOException {
		shouldCompileTo("{{{asset \"js/all.min.js\"}}}", this, "js/all-8891825da6.min.js");
	}

	@Test
	public void testCssAsset() throws IOException {
		shouldCompileTo("{{{asset \"css/style.min.css\"}}}", this, "css/style-11659a4ba9.min.css");
	}
}
