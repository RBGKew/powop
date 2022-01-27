package org.powo.portal.service;

import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.portal.view.helpers.NameHelper;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ImageCaptionServiceTest {
	private ImageCaptionService imageCaptionService;
	private NameHelper nameHelper;

	@Before
	public void init() {
		nameHelper = createMock(NameHelper.class);
		imageCaptionService = new ImageCaptionService(nameHelper);
	}

	@Test
	public void getHtmlCaptionTitleOnly() {
		var image = new Image();
		image.setTitle("A flower");

		var result = imageCaptionService.getHtmlCaption(image, null);

		assertEquals("A flower<small></small>", result);
	}

	@Test
	public void getHtmlCaptionTitleAndCaption() {
		var image = new Image();
		image.setTitle("A flower");
		image.setCaption("Picked by a mongoose");

		var result = imageCaptionService.getHtmlCaption(image, null);

		assertEquals("A flower - Picked by a mongoose<small></small>", result);
	}

	@Test
	public void getHtmlCaptionSameTaxonShouldNotAddTaxonLink() {
		var taxon = new Taxon();
		var image = new Image();
		image.setTitle("A flower");
		image.setTaxon(taxon);

		var result = imageCaptionService.getHtmlCaption(image, taxon);

		assertEquals("A flower<small></small>", result);
	}

	@Test
	public void getHtmlCaptionDifferentTaxonShouldNotAddTaxonLink() {
		var taxon1 = new Taxon();
		var taxon2 = new Taxon();
		var image = new Image();
		image.setTitle("A flower");
		image.setTaxon(taxon1);
		expect(nameHelper.taxonLinkWithoutAuthor(anyObject(), anyObject())).andReturn("Bromus abolinii").anyTimes();
		replay(nameHelper);

		var result = imageCaptionService.getHtmlCaption(image, taxon2);

		assertEquals("A flower<small>Bromus abolinii | </small>", result);
	}

	@Test
	public void getHtmlCaptionAddsCreator() {
		var image = new Image();
		image.setTitle("A flower");
		image.setCreator("Malcolm");

		var result = imageCaptionService.getHtmlCaption(image, null);

		assertEquals("A flower<small>Malcolm</small>", result);
	}

	@Test
	public void getHtmlCaptionAddsOwner() {
		var image = new Image();
		image.setTitle("A flower");
		image.setOwner("Kew");

		var result = imageCaptionService.getHtmlCaption(image, null);

		assertEquals("A flower<small>Kew</small>", result);
	}

	@Test
	public void getHtmlCaptionAddsSource() {
		var image = new Image();
		image.setTitle("A flower");
		image.setSource("Digifolia");

		var result = imageCaptionService.getHtmlCaption(image, null);

		assertEquals("A flower<small>Digifolia</small>", result);
	}

	@Test
	public void getHtmlCaptionAddsCreatorOverOwnerSource() {
		var image = new Image();
		image.setTitle("A flower");
		image.setCreator("Malcolm");
		image.setOwner("Kew");
		image.setSource("Digifolia");

		var result = imageCaptionService.getHtmlCaption(image, null);

		assertEquals("A flower<small>Malcolm</small>", result);
	}
	
}
