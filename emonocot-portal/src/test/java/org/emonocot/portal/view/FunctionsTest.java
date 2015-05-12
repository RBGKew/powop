/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.portal.view;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.gbif.ecat.voc.Rank;
import org.junit.Test;
import org.emonocot.model.Description;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;

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
	public void testEscapeXml() {
		assertEquals("'&' should be escaped as '&amp;'",Functions.escape("&"),"&amp;");
		assertEquals("''' should be escaped as '&apos;'",Functions.escape("'"),"&apos;");
		assertEquals("'\"' should be escaped as '&quot;'",Functions.escape("\""),"&quot;");
	}
	
	@Test
	public void testFormatDateRange() {
		assertEquals("Date range should be formatted as expected",Functions.formatDateRange("[2012-09-12T00:00:00Z TO 2012-09-12T00:00:00Z+1MONTH]"),"2012/09 - 2012/10");
	}
	
	@Test
	public void testSortDescriptions() {
		Taxon taxon = new Taxon();

		taxon.getDescriptions().add(createDescription("description1", DescriptionType.associations));
		taxon.getDescriptions().add(createDescription("description3", DescriptionType.associations));
		taxon.getDescriptions().add(createDescription("description2", DescriptionType.general));
		
		assertEquals("This function should return more than one description of the same type", Functions.content(taxon, DescriptionType.associations).size(),2);
	}

	private Description createDescription(String identifier, DescriptionType type) {
		Description description = new Description();
		description.setIdentifier(identifier);
		description.setType(type);
		return description;
	}
}
