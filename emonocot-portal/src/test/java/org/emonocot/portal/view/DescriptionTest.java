package org.emonocot.portal.view;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;

import org.emonocot.model.Description;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.registry.Organisation;
import org.emonocot.portal.view.Descriptions.DescriptionsBySource;
import org.emonocot.portal.view.Descriptions.DescriptionsByType;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

public class DescriptionTest {

	@Test
	public void testBySource() {
		Taxon taxon = new Taxon();
		Description d1 = new Description();
		Description d2 = new Description();
		Description d3 = new Description();
		Organisation o1 = new Organisation();
		Organisation o2 = new Organisation();

		o1.setAbbreviation("FWTA");
		o1.setTitle("Flora of West Tropical Africa");

		o2.setAbbreviation("FTEA");
		o2.setTitle("Flora of Tropical East Africa");

		d1.setType(DescriptionType.morphologyGeneralHabit);
		d1.setDescription("Perenial herb");
		d1.setAuthority(o1);

		d2.setType(DescriptionType.morphologyLeaf);
		d2.setDescription("Basal leaves");
		d2.setAuthority(o1);

		d3.setType(DescriptionType.morphologyReproductiveFlowers);
		d3.setDescription("Large");
		d3.setAuthority(o2);

		taxon.setDescriptions(ImmutableSet.<Description>of(d1, d2, d3));

		Descriptions ds = new Descriptions(taxon);
		for(DescriptionsBySource dbs : ds.getBySource()) {
			System.out.print("{" + dbs.source + ": ");
			for(DescriptionsByType dbt : dbs.byType) {
				System.out.print(dbt.type + " [");
				for(Description d : dbt.descriptions) {
					System.out.print(d.getDescription() + " ");
				}
				System.out.print("]");
			}
			System.out.println("}");
		}

		Collection<DescriptionsBySource> dbs = ds.getBySource();
		assertEquals(2, dbs.size());
		Iterator<DescriptionsBySource> iter = dbs.iterator();

		DescriptionsBySource fwta = iter.next();
		assertEquals("FTEA", fwta.source.getAbbreviation());
	}
}
