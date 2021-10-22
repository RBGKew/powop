package org.powo.portal.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.gbif.ecat.voc.Rank;
import org.junit.Test;
import org.powo.model.Description;
import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.model.constants.DescriptionType;
import org.powo.model.constants.TaxonomicStatus;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import jersey.repackaged.com.google.common.collect.Sets;

public class FeaturedTaxonTest {

	MessageSource messageSource = new ReloadableResourceBundleMessageSource();

  @Test
  public void testFeaturedImage() {
    var featuredTaxon = new FeaturedTaxon(null, messageSource);
    assertNull(featuredTaxon.getFeaturedImage());

    var taxon = new Taxon();
    featuredTaxon.setTaxon(taxon);
    assertNull(featuredTaxon.getFeaturedImage());

    var images = new ArrayList<Image>();
    taxon.setImages(images);
    assertNull(featuredTaxon.getFeaturedImage());

    var image = new Image();
    image.setTitle("Test image");
    images.add(image);

    assertEquals(featuredTaxon.getFeaturedImage().getTitle(), "Test image");

  }

  @Test
  public void testSummary() {
    var featuredTaxon = new FeaturedTaxon(null, messageSource);
    assertEquals("", featuredTaxon.getSummary());

    var taxon = new Taxon();
    featuredTaxon.setTaxon(taxon);

    // Test auto-generated Summary
    taxon.setTaxonRank(Rank.SPECIES);
    taxon.setTaxonomicStatus(TaxonomicStatus.Accepted);
		assertEquals("This species is accepted.", featuredTaxon.getSummary());

    // Test curated Summary
    var summaryDescription = new Description();
    summaryDescription.setType(DescriptionType.summary);
    summaryDescription.setDescription("Test summary");
    taxon.setDescriptions(Sets.newHashSet(summaryDescription));
    assertEquals("Test summary", featuredTaxon.getSummary());
  }

}
