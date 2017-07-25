package org.emonocot.portal.view.helpers;

import java.io.IOException;

import org.emonocot.model.Image;
import org.junit.Before;
import org.junit.Test;

import com.github.jknack.handlebars.Handlebars;

public class ImageHelperTest extends AbstractHelperTest {

	private Image image;

	@Override
	protected Handlebars newHandlebars() {
		Handlebars handlebars = super.newHandlebars();
		handlebars.registerHelpers(new ImageHelper("test", "https://cdn.com"));
		return handlebars;
	}

	@Before
	public void setUp() {
		image = new Image();
		image.setAccessUri("http://assets.blah.com/cool-img");
		image.setTitle("Cool brah");
		image.setCaption("Coolio");
		image.setCreator("Some dude");
		image.setIdentifier("urn:kew.org:blargh");
	}

	@Test
	public void basicThumbnailImage() throws IOException {
		shouldCompileTo("{{thumbnailImage this lightbox=false}}", image, "<img src=\"http://assets.blah.com/cool-img_thumbnail.jpg\" title=\"Cool brah\"/>");
	}

	@Test
	public void thumbnailWithLightbox() throws IOException {
		shouldCompileTo("{{thumbnailImage this}}", image,
				"<a href=\"http://assets.blah.com/cool-img_fullsize.jpg\" title=\"Cool brah - Coolio<small>Some dude</small>\">" +
				"<img src=\"http://assets.blah.com/cool-img_thumbnail.jpg\" title=\"Cool brah\"/></a>");
	}

	@Test
	public void thumbnailInFigureWithLightbox() throws IOException {
		shouldCompileTo("{{thumbnailImage this figure-class=\"woo\"}}", image,
				"<a href=\"http://assets.blah.com/cool-img_fullsize.jpg\" title=\"Cool brah - Coolio<small>Some dude</small>\">" +
				"<figure class=\"woo\"><img src=\"http://assets.blah.com/cool-img_thumbnail.jpg\" title=\"Cool brah\"/></figure></a>");
	}

	@Test
	public void digifoliaThumbnail() throws IOException {
		image = new Image();
		image.setIdentifier("urn:kew.org:dam:654");
		shouldCompileTo("{{thumbnailImage this}}", image,
				"<a href=\"https://cdn.com/13a994141177e43d57feb31d29f1e9b7.jpg\" title=\"<small></small>\">" +
				"<img src=\"https://cdn.com/936cd5acff71ed403b5b6e1b0fa0b127.jpg\" title=\"\"/></a>");
	}
}