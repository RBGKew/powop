package org.powo.portal.view.helpers;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.model.helpers.CDNImageHelper;
import org.junit.Before;
import org.junit.Test;

import com.github.jknack.handlebars.Handlebars;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ImageHelperTest extends AbstractHelperTest {

	@Data
	@AllArgsConstructor
	private class Context {
		private Image image;
		private Taxon taxon;
	}

	private Context context;

	@Override
	protected Handlebars newHandlebars() {
		String[] secureDomains = {"cdn.com"};
		CDNImageHelper cdn = new CDNImageHelper("test", "https://cdn.com", secureDomains);
		Handlebars handlebars = super.newHandlebars();
		handlebars.registerHelpers(new ImageHelper(cdn));
		return handlebars;
	}

	@Before
	public void setUp() {
		Image image = new Image();
		image.setAccessUri("http://assets.blah.com/cool-img");
		image.setTitle("Cool brah");
		image.setCaption("Coolio");
		image.setCreator("Some dude");
		image.setIdentifier("urn:kew.org:blargh");

		Taxon taxon = new Taxon();
		taxon.setIdentifier("123");

		this.context = new Context(image, taxon);
	}

	@Test
	public void testImageToFullsizeUrl() throws IOException {
		var result = renderTemplate("{{imageToThumbnailUrl image}}", context);
		assertEquals("http://assets.blah.com/cool-img_thumbnail.jpg", result);
	}

	@Test
	public void testImageToThumbnailUrl() throws IOException {
		var result = renderTemplate("{{imageToFullsizeUrl image}}", context);
		assertEquals("http://assets.blah.com/cool-img_fullsize.jpg", result);
	}
}