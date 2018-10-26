package org.powo.portal.view;

import static com.google.common.collect.ImmutableSet.of;
import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.powo.model.Taxon;
import org.powo.model.VernacularName;
import org.junit.Before;
import org.junit.Test;
import org.powo.portal.view.VernacularNames;

public class VernacularNamesTest {

	private VernacularNames names;
	private Taxon taxon;

	@Before
	public void setUp() {
		taxon = new Taxon();

		VernacularName n1 = new VernacularName();
		n1.setVernacularName("Sedge");
		n1.setLanguage(new Locale("en"));
		n1.setTaxon(taxon);

		VernacularName n2 = new VernacularName();
		n2.setVernacularName("швар");
		n2.setLanguage(new Locale("uk"));
		n2.setTaxon(taxon);

		VernacularName n3 = new VernacularName();
		n3.setVernacularName("starrslekta");
		n3.setTaxon(taxon);

		VernacularName n4 = new VernacularName();
		n4.setVernacularName("starrslekta2");
		n4.setLanguage(new Locale(""));
		n4.setTaxon(taxon);

		taxon.setVernacularNames(of(n1, n2, n3, n4));
	}

	@Test
	public void testSortedByLanguage() {
		names = new VernacularNames(taxon);

		assertEquals("Sedge", names.getSortedByLanguage().get("English").first());
		assertEquals("швар", names.getSortedByLanguage().get("Ukrainian").first());
		assertEquals("starrslekta", names.getSortedByLanguage().get("Unknown").first());
		assertEquals("starrslekta2", names.getSortedByLanguage().get("Unknown").last());
	}

	@Test
	public void testIncludesNamesFromSynonym() {
		Taxon synonym = new Taxon();

		VernacularName n1 = new VernacularName();
		n1.setVernacularName("Nut Sedge");
		n1.setLanguage(new Locale("en"));
		n1.setTaxon(synonym);
		synonym.setVernacularNames(of(n1));
		taxon.setSynonymNameUsages(of(synonym));

		names = new VernacularNames(taxon);

		assertEquals("Nut Sedge", names.getSortedByLanguage().get("English").first());
		assertEquals("Sedge", names.getSortedByLanguage().get("English").last());
		assertEquals("швар", names.getSortedByLanguage().get("Ukrainian").first());
		assertEquals("starrslekta", names.getSortedByLanguage().get("Unknown").first());
		assertEquals("starrslekta2", names.getSortedByLanguage().get("Unknown").last());
	}
}
