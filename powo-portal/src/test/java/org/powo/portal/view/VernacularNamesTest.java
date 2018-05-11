package org.powo.portal.view;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.powo.model.Taxon;
import org.powo.model.VernacularName;
import org.junit.Before;
import org.junit.Test;
import org.powo.portal.view.VernacularNames;

import com.google.common.collect.ImmutableSet;

public class VernacularNamesTest {

	private VernacularNames names;

	@Before
	public void setUp() {
		Taxon taxon = new Taxon();

		VernacularName n1 = new VernacularName();
		n1.setVernacularName("Sedge");
		n1.setLanguage(new Locale("en"));

		VernacularName n2 = new VernacularName();
		n2.setVernacularName("швар");
		n2.setLanguage(new Locale("uk"));

		VernacularName n3 = new VernacularName();
		n3.setVernacularName("starrslekta");
		n3.setLanguage(new Locale(""));

		taxon.setVernacularNames(ImmutableSet.<VernacularName>of(n1, n2, n3));

		names = new VernacularNames(taxon);
	}

	@Test
	public void testSortedByLanguage () {
		assertEquals("Sedge", names.getSortedByLanguage().get("English").first());
		assertEquals("швар", names.getSortedByLanguage().get("Ukrainian").first());
		assertEquals("starrslekta", names.getSortedByLanguage().get("Unknown").first());
	}
}
