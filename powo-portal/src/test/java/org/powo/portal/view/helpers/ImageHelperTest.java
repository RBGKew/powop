package org.powo.portal.view.helpers;

import java.io.IOException;

import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.model.helpers.CDNImageHelper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.powo.portal.view.helpers.ImageHelper;

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
		CDNImageHelper cdn = new CDNImageHelper("test", "https://cdn.com");
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
	public void basicThumbnailImage() throws IOException {
		shouldCompileTo("{{thumbnailImage image taxon lightbox=false}}", context,
				"<img src=\"http://assets.blah.com/cool-img_thumbnail.jpg\" alt=\"Cool brah\"/>");
	}

	@Test
	public void thumbnailWithLightbox() throws IOException {
		shouldCompileTo("{{thumbnailImage image taxon}}", context,
				"<a href=\"http://assets.blah.com/cool-img_fullsize.jpg\" alt=\"Cool brah - Coolio<small>Some dude</small>\">" +
				"<img src=\"http://assets.blah.com/cool-img_thumbnail.jpg\" alt=\"Cool brah\"/></a>");
	}

	@Test
	public void thumbnailInFigureWithLightbox() throws IOException {
		shouldCompileTo("{{thumbnailImage image taxon figure-class=\"woo\"}}", context,
				"<a href=\"http://assets.blah.com/cool-img_fullsize.jpg\" alt=\"Cool brah - Coolio<small>Some dude</small>\">" +
				"<figure class=\"woo\"><img src=\"http://assets.blah.com/cool-img_thumbnail.jpg\" alt=\"Cool brah\"/></figure></a>");
	}

	@Test
	public void digifoliaThumbnail() throws IOException {
		Image image = new Image();
		image.setIdentifier("urn:kew.org:dam:654");
		context.setImage(image);
		shouldCompileTo("{{thumbnailImage image taxon}}", context,
				"<a href=\"https://cdn.com/13a994141177e43d57feb31d29f1e9b7.jpg\" alt=\"<small></small>\">" +
				"<img src=\"https://cdn.com/936cd5acff71ed403b5b6e1b0fa0b127.jpg\" alt=\"\"/></a>");
	}

	@Test
	public void imageOnDifferentTaxonPage() throws IOException {
		Taxon taxon = new Taxon();
		taxon.setIdentifier("456");
		taxon.setScientificName("Poa annua");
		context.getImage().setTaxon(taxon);

		shouldCompileTo("{{thumbnailImage image taxon}}", context,
				"<a href=\"http://assets.blah.com/cool-img_fullsize.jpg\" alt=\"Cool brah - Coolio<small><a href='/taxon/456'><em lang='la'>Poa annua</em></a> | Some dude</small>\">" +
				"<img src=\"http://assets.blah.com/cool-img_thumbnail.jpg\" alt=\"Cool brah\"/></a>");
	}

	@Test
	public void imageWithQuotesInCaption() throws IOException {
		context.getImage().setCaption("<a href=\"google.com\">a link</a>");
		shouldCompileTo("{{thumbnailImage image taxon}}", context,
				"<a href=\"http://assets.blah.com/cool-img_fullsize.jpg\" alt=\"Cool brah - <a href='google.com'>a link</a><small>Some dude</small>\">" +
				"<img src=\"http://assets.blah.com/cool-img_thumbnail.jpg\" alt=\"Cool brah\"/></a>");
	}

	@Test
	public void imageWithQuotesInTitle() throws IOException {
		context.getImage().setTitle("\"some title\"");
		shouldCompileTo("{{thumbnailImage image taxon}}", context,
				"<a href=\"http://assets.blah.com/cool-img_fullsize.jpg\" alt=\"'some title' - Coolio<small>Some dude</small>\">" +
				"<img src=\"http://assets.blah.com/cool-img_thumbnail.jpg\" alt=\"'some title'\"/></a>");
	}
}