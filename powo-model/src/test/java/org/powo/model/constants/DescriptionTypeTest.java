package org.powo.model.constants;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.powo.model.constants.DescriptionType.*;

import com.google.common.collect.Lists;

import org.junit.Test;

public class DescriptionTypeTest {

	@Test
	public void testFromUriString() {
		assertEquals(fromString("http://rs.gbif.org/vocabulary/gbif/descriptionType/habitat"), habitat);
	}

	@Test
	public void testFromIdentifier() {
		assertEquals(fromString("habitat"), habitat);
	}

	@Test
	public void testFromHierarchicalIdentifier() {
		assertEquals(fromString("morphology:reproductive:flower:perianth"), morphologyReproductiveFlowerPerianth);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnknownUri() {
		fromString("http://totally.not.a.real.url.com/habitat");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnknownIdentifier() {
		fromString("notARealDescription");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidHierarchy() {
		fromString("morphology:leaf:flowers:corolla");
	}

	@Test
	public void testGetAll() {
		assertThat(getAll(use), hasItems(use, useAnimalFoodFlowers, useFoodStarches));
	}

	@Test
	public void testIsA() {
		assertTrue(morphologyReproductiveFlower.isA(morphology));
		assertTrue(useAnimalFoodGrainsCereals.isA(use));
		assertTrue(use.isA(use));
	}

	@Test
	public void testGetTypeHeirarchy() {
		assertTrue(morphologyReproductiveFlower.isA(morphology));
		assertTrue(useAnimalFoodGrainsCereals.isA(use));
		assertTrue(use.isA(use));

		assertEquals(Lists.newArrayList(use, useAnimalFood, useAnimalFoodGrainsCereals), useAnimalFoodGrainsCereals.getTypeHeirarchy());
	}
}
