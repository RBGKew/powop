package org.powo.portal.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.junit.Test;
import org.powo.model.Description;
import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.model.constants.DescriptionType;

public class FeaturedTaxonTest {

  @Test
  public void testFeaturedImage() {
    var featuredTaxon = new FeaturedTaxon(null);
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
  public void testDescriptions() {
    var featuredTaxon = new FeaturedTaxon(null);
    assertNull(featuredTaxon.getDescriptions());

    var taxon = new Taxon();
    featuredTaxon.setTaxon(taxon);
    assertEquals(featuredTaxon.getDescriptions().size(), 0);

    var morphDescription = new Description();
    morphDescription.setType(DescriptionType.morphology);
    morphDescription.setDescription("Round leaves");
    var beveragesDescription = new Description();
    beveragesDescription.setType(DescriptionType.useFoodBeverages);
    beveragesDescription.setDescription("Tea");
    var foodDescription = new Description();
    foodDescription.setType(DescriptionType.useFood);
    foodDescription.setDescription("Food");

    taxon.setDescriptions(Sets.newHashSet(morphDescription, beveragesDescription, foodDescription));
    assertEquals(Lists.newArrayList(morphDescription, foodDescription, beveragesDescription),
        featuredTaxon.getDescriptions());

    featuredTaxon.setPriorityDescriptionType(DescriptionType.morphology);
    assertEquals(Lists.newArrayList(morphDescription, foodDescription, beveragesDescription),
        featuredTaxon.getDescriptions());

    featuredTaxon.setPriorityDescriptionType(DescriptionType.use);
    assertEquals(Lists.newArrayList(foodDescription, beveragesDescription, morphDescription),
        featuredTaxon.getDescriptions());
  }

}
