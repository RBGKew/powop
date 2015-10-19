package org.emonocot.model.constants;

import static org.junit.Assert.assertEquals;

import org.emonocot.model.constants.DescriptionType;
import org.junit.Test;

public class DescriptionTypeTest {

	@Test
	public void testFromUriString() {
		assertEquals(
				DescriptionType.fromString("http://rs.gbif.org/vocabulary/gbif/descriptionType/habitat"),
				DescriptionType.habitat);
	}

	@Test
	public void testFromIdentifier() {
		assertEquals(
				DescriptionType.fromString("habitat"),
				DescriptionType.habitat);
	}

	@Test
	public void testFromHierarchicalIdentifier() {
		assertEquals(
				DescriptionType.fromString("reproductiveMorphology:flowers:perianth"),
				DescriptionType.perianth);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnknownUri() {
		DescriptionType.fromString("http://totally.not.a.real.url.com/habitat");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnknownIdentifier() {
		DescriptionType.fromString("notARealDescription");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidHierarchy() {
		DescriptionType.fromString("leafMorphology:flowers:corolla");
	}
}
