package org.emonocot.model.constants;

import static org.emonocot.model.constants.DescriptionType.*;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
		assertEquals(fromString("morphology:reproductive:flowers:perianth"), morphologyReproductiveFlowersPerianth);
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
		assertTrue(morphologyReproductiveFlowers.isA(morphology));
		assertTrue(useAnimalFoodGrainsCereals.isA(use));
		assertTrue(use.isA(use));
	}
}
