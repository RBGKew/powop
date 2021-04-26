package org.powo.portal.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Test;
import org.powo.model.Image;
import org.powo.model.Taxon;

public class FeaturedTaxonTest {

  @Test
  public void testFeaturedImage() {
    var taxon = new Taxon();
    var featuredTaxon = new FeaturedTaxon(taxon);
    assertNull(featuredTaxon.getFeaturedImage());

    var images = new ArrayList<Image>();
    taxon.setImages(images);
    assertNull(featuredTaxon.getFeaturedImage());

    var image = new Image();
    image.setTitle("Test image");
    images.add(image);

    assertEquals(featuredTaxon.getFeaturedImage().getTitle(), "Test image");

  }

}
