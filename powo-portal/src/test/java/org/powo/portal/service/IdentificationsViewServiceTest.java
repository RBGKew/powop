package org.powo.portal.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.powo.model.Identification;
import org.powo.model.Taxon;
import org.powo.portal.view.helpers.NameHelper;

public class IdentificationsViewServiceTest {
	private IdentificationsViewService identificationsViewService;

	@Before
	public void init() {
		identificationsViewService = new IdentificationsViewService(new NameHelper());
	}

	@Test
	public void testGetIdentifications() {
		var taxon = new Taxon();
		var identification = new Identification();
		identification.setTaxon(taxon);
		identification.setIdentifiedBy("A");
		identification.setIdentificationReferences("B");
		identification.setTypeStatus("C");
		taxon.setIdentifications(Set.of(identification));

		var result = identificationsViewService.getIdentifications(taxon);
		assertEquals(1, result.size());

		var identificationView = result.get(0);
		assertEquals("A", identificationView.getUrl());
		assertEquals("B", identificationView.getNotes());
		assertEquals("C", identificationView.getTypeStatus());
		assertNull(identificationView.getBarcode());
	}

	@Test
	public void testGetIdentificationsWithSynonyms() {
		var synonym = new Taxon();
		synonym.setIdentifier("ABCDE");
		var identification = new Identification();
		identification.setTaxon(synonym);
		synonym.setIdentifications(Set.of(identification));
		var taxon = new Taxon();
		taxon.setSynonymNameUsages(Set.of(synonym));

		var result = identificationsViewService.getIdentifications(taxon);
		assertEquals(1, result.size());
		
		var identificationView = result.get(0);
		assertEquals("<a href=\"/taxon/ABCDE\"></a>", identificationView.getIdentifiedAs());
	}
}
