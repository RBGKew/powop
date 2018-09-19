package org.powo.portal.v2.json;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.gbif.ecat.voc.EstablishmentMeans;
import org.gbif.ecat.voc.Rank;
import org.junit.Test;
import org.powo.model.Distribution;
import org.powo.model.Taxon;
import org.powo.model.constants.Location;
import org.powo.model.constants.TaxonField;
import org.powo.model.constants.TaxonomicStatus;
import org.powo.portal.json.v2.TaxonResponse;
import org.powo.portal.view.Distributions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class TaxonResponseTest {

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

	private Distribution buildDistribution(Location loc, EstablishmentMeans est) {
		Distribution d = new Distribution();
		d.setLocation(loc);
		d.setEstablishmentMeans(est);

		return d;
	}

	@Test
	public void testBasicTaxon() {
		Taxon t = baseTaxon();
		TaxonResponse tw = new TaxonResponse(t);

		assertEquals(t.getIdentifier(), tw.getOutput().get("identifier").toString());
	}

	@Test
	public void testTaxonWithDistributions() {
		Taxon t = baseTaxon();
		List<Distribution> distributions = ImmutableList.of(
				buildDistribution(Location.GRB, EstablishmentMeans.Native),
				buildDistribution(Location.IRE, EstablishmentMeans.Introduced));
		t.setDistribution(ImmutableSet.copyOf(distributions));

		TaxonResponse tw = new TaxonResponse(t, ImmutableList.of(TaxonField.distribution));

		Distributions distr = (Distributions) tw.getOutput().get("distribution");
		assertEquals(distributions.get(0), distr.getNatives().get(0));
		assertEquals(distributions.get(1), distr.getIntroduced().get(0));
	}
}
