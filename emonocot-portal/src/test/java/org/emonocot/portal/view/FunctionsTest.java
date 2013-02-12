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
	
	@Test
	public void testEscapeHtmlIdentifier() {
		assertEquals("'base.class_s' should be escaped as 'baseclasss'",Functions.escapeHtmlIdentifier("base.class_s"),"baseclasss");
	}
	
	@Test
	public void testFormatDateRange() {
		assertEquals("Date range should be formatted as expected",Functions.formatDateRange("[2012-09-12T00:00:00Z TO 2012-09-12T00:00:00Z+1MONTH]"),"2012/09 - 2012/10");
	}
}
