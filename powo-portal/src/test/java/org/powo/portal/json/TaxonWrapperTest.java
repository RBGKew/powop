package org.powo.portal.json;

import static org.junit.Assert.assertEquals;

import org.gbif.ecat.voc.Rank;
import org.junit.Test;
import org.powo.model.Taxon;
import org.powo.model.constants.TaxonomicStatus;

public class TaxonWrapperTest {
	
	private Taxon baseTaxon() {
		Taxon t = new Taxon();
		t.setIdentifier("urn:lsid:ipni.org:123");
		t.setScientificName("Poa annua");
		t.setScientificNameAuthorship("L.");
		t.setFamily("Poaceae");
		t.setGenus("Poa");
		t.setSpecificEpithet("annua");
		t.setTaxonRank(Rank.SPECIES);
		t.setTaxonomicStatus(TaxonomicStatus.Accepted);
		t.setNamePublishedInString("Sp. Pl.: 68 (1753)");
		return t;
	}
	
	private 

	@Test
	public void testBasicTaxon() {
		Taxon t = baseTaxon();
		TaxonWrapper tw = new TaxonWrapper(t);

		assertEquals(t.getIdentifier(), tw.getOutput().get("identifier").toString());
	}

	@Test
	public void testTaxonWithSources() {
		Taxon t = baseTaxon();
	}
}
