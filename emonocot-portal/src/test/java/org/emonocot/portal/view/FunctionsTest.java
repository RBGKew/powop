package org.emonocot.portal.view;

import static org.junit.Assert.assertEquals;

import org.gbif.ecat.voc.Rank;
import org.junit.Test;

public class FunctionsTest {
	
	@Test
	public void testFormatRank() {
		assertEquals("FAMILY should be formatted as Family",Functions.formatRank(Rank.FAMILY),"Family");
		assertEquals("GENUS should be formatted as Genus",Functions.formatRank(Rank.GENUS),"Genus");
		assertEquals("SPECIES should be formatted as Species",Functions.formatRank(Rank.SPECIES),"Species");
	}

	@Test
	public void testAbbreviateRank() {
		assertEquals("FAMILY should be formatted as Family",Functions.abbreviateRank(Rank.FAMILY),"fam");
		assertEquals("GENUS should be formatted as Genus",Functions.abbreviateRank(Rank.GENUS),"gen");
		assertEquals("SPECIES should be formatted as Species",Functions.abbreviateRank(Rank.SPECIES),"sp");
	}
}
