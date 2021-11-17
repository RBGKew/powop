package org.powo.portal.view.helpers;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import org.powo.model.Image;
import org.powo.model.helpers.CDNImageHelper;
import org.junit.Test;

import com.github.jknack.handlebars.Handlebars;

public class ImageHelperTest extends AbstractHelperTest {
	@Override
	protected Handlebars newHandlebars() {
		String[] secureDomains = {"cdn.com"};
		var cdn = new CDNImageHelper("test", "https://cdn.com", secureDomains);
		var handlebars = super.newHandlebars();
		handlebars.registerHelpers(new ImageHelper(cdn));
		return handlebars;
	}

	@Test
	public void testImageToFullsizeUrl() throws IOException {
		var image = new Image();
		image.setAccessUri("http://assets.blah.com/cool-img");
		image.setIdentifier("urn:kew.org:blargh");

		var result = renderTemplate("{{imageToThumbnailUrl image}}", Map.of("image", image));
		assertEquals("http://assets.blah.com/cool-img_thumbnail.jpg", result);
	}

	@Test
	public void testImageToThumbnailUrl() throws IOException {
		var image = new Image();
		image.setAccessUri("http://assets.blah.com/cool-img");
		image.setIdentifier("urn:kew.org:blargh");

		var result = renderTemplate("{{imageToFullsizeUrl image}}", Map.of("image", image));
		assertEquals("http://assets.blah.com/cool-img_fullsize.jpg", result);
	}
}