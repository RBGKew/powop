package org.powo.portal.service;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powo.api.ImageService;
import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.model.helpers.CDNImageHelper;
import org.powo.model.registry.Organisation;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Set;

public class TaxonImageServiceTest {
	private ImageService imageService;
	private CDNImageHelper cdnImageHelper;
	private TaxonImageService taxonImageService;

	@Before
	public void init() {
		imageService = createMock(ImageService.class);
		cdnImageHelper = createMock(CDNImageHelper.class);
		taxonImageService = new TaxonImageService(imageService, cdnImageHelper, null);
	}

	@Test
	public void testGetTaxonImageSetNoImages() {
		var taxon = new Taxon();
		expect(imageService.getTopImages(eq(taxon), anyInt())).andReturn(List.of());
		replay(imageService);

		var imageSet = taxonImageService.getTaxonImageSet(taxon);

		assertEquals(0, imageSet.getImages().size());
		assertEquals(0, imageSet.getSources().size());
		assertNull(imageSet.getHeaderImage());
	}
	
	@Test
	public void testGetTaxonImageSetManyImages() {
		var taxon = new Taxon();
		var images = List.of(
			new Image(), new Image(), new Image(),
			new Image(), new Image(), new Image()			
		);
		taxon.setImages(images);
		expect(cdnImageHelper.getFullsizeUrl(anyObject(Image.class))).andReturn("").anyTimes();
		expect(cdnImageHelper.getThumbnailUrl(anyObject(Image.class))).andReturn("").anyTimes();
		replay(cdnImageHelper);

		var imageSet = taxonImageService.getTaxonImageSet(taxon);

		assertEquals(6, imageSet.getImages().size());
		assertEquals(1, imageSet.getSources().size());
		assertNotNull(imageSet.getHeaderImage());
	}
	
	@Test
	public void testGetTaxonImageSetAddsTopImagesIfFewerThanSix() {
		var taxon = new Taxon();
		var images = List.of(
			new Image(), new Image(), new Image()
		);
		taxon.setImages(images);
		var topImages = List.of(
			new Image(), new Image(), new Image()
		);

		expect(imageService.getTopImages(eq(taxon), anyInt())).andReturn(topImages);
		expect(cdnImageHelper.getFullsizeUrl(anyObject(Image.class))).andReturn("").anyTimes();
		expect(cdnImageHelper.getThumbnailUrl(anyObject(Image.class))).andReturn("").anyTimes();
		replay(cdnImageHelper, imageService);

		var imageSet = taxonImageService.getTaxonImageSet(taxon);

		assertEquals(6, imageSet.getImages().size());
		assertEquals(1, imageSet.getSources().size());
		assertNotNull(imageSet.getHeaderImage());
	}
	
	@Test
	public void testGetTaxonImageSetAddsSynonymImages() {
		var taxon = new Taxon();
		var images = List.of(
			new Image(), new Image(), new Image()
		);
		taxon.setImages(images);
		var synonym = new Taxon();
		var synonymImages = List.of(
			new Image(), new Image(), new Image()
		);
		synonym.setImages(synonymImages);
		taxon.setSynonymNameUsages(Set.of(synonym));

		expect(cdnImageHelper.getFullsizeUrl(anyObject(Image.class))).andReturn("").anyTimes();
		expect(cdnImageHelper.getThumbnailUrl(anyObject(Image.class))).andReturn("").anyTimes();
		replay(cdnImageHelper);

		var imageSet = taxonImageService.getTaxonImageSet(taxon);

		assertEquals(6, imageSet.getImages().size());
		assertEquals(1, imageSet.getSources().size());
		assertNotNull(imageSet.getHeaderImage());
	}

	@Test
	public void testGetTaxonImageSetAddsSourcesFromImages() {
		var taxon = new Taxon();
		var org1 = new Organisation();
		var org2 = new Organisation();
		var image1 = new Image();
		image1.setAuthority(org1);
		var image2 = new Image();
		image2.setAuthority(org2);
		var image3 = new Image();
		image3.setAuthority(org1);
		taxon.setImages(List.of(image1, image2, image3));

		expect(imageService.getTopImages(eq(taxon), anyInt())).andReturn(List.of());
		expect(cdnImageHelper.getFullsizeUrl(anyObject(Image.class))).andReturn("").anyTimes();
		expect(cdnImageHelper.getThumbnailUrl(anyObject(Image.class))).andReturn("").anyTimes();
		replay(cdnImageHelper, imageService);

		var imageSet = taxonImageService.getTaxonImageSet(taxon);

		assertEquals(3, imageSet.getImages().size());
		assertEquals(2, imageSet.getSources().size());
		assertNotNull(imageSet.getHeaderImage());
	}
}
