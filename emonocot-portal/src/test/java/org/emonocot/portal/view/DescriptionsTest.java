package org.emonocot.portal.view;

import static org.junit.Assert.assertEquals;
import static org.emonocot.model.constants.DescriptionType.*;

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
import com.google.common.collect.ImmutableSortedSet;

public class DescriptionsTest {

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
		print(ds);

		Collection<DescriptionsBySource> dbs = ds.getBySource();
		assertEquals(2, dbs.size());
		Iterator<DescriptionsBySource> iter = dbs.iterator();

		DescriptionsBySource fwta = iter.next();
		assertEquals("FTEA", fwta.source.getAbbreviation());
	}

	@Test
	public void testDescriptionTypeFiltering() {
		Taxon taxon = new Taxon();
		Description u1 = new Description();
		Description u2 = new Description();
		Organisation o1 = new Organisation();

		o1.setAbbreviation("FWTA");
		o1.setTitle("Flora of West Tropical Africa");

		u1.setType(useAnimalFoodBees);
		u1.setDescription("BEES!!!");
		u1.setAuthority(o1);

		u2.setType(useMedicinesDigestiveSystemDisorders);
		u2.setDescription("You have died of dysentery");
		u2.setAuthority(o1);

		taxon.setDescriptions(ImmutableSet.<Description>of(u1, u2));

		Descriptions ds = new Descriptions(taxon, true);
		print(ds);
	}

	@Test
	public void testMultiTypeDescriptions() {
		Taxon taxon = new Taxon();
		Description u1 = new Description();
		Organisation o1 = new Organisation();

		o1.setAbbreviation("FWTA");
		o1.setTitle("Flora of West Tropical Africa");

		u1.setTypes(ImmutableSortedSet.<DescriptionType>of(useAnimalFoodBees, morphologyReproductiveFlowers));
		u1.setDescription("BEES!!!");
		u1.setAuthority(o1);

		taxon.setDescriptions(ImmutableSet.<Description>of(u1));

		Descriptions uses = new Descriptions(taxon, true);
		for(DescriptionsBySource dbs : uses.getBySource()) {
			for(DescriptionsByType dbt : dbs.byType) {
				assertEquals("useAnimalFoodBees", dbt.type);
			}
		}

		// Data tagged with use should not show up in the morphological descriptions
		Descriptions desc = new Descriptions(taxon);
		assertEquals(0, desc.getBySource().size());
	}

	private void print(Descriptions descriptions) {
		for(DescriptionsBySource dbs : descriptions.getBySource()) {
			System.out.print("{" + dbs.source.getTitle() + ": ");
			for(DescriptionsByType dbt : dbs.byType) {
				System.out.print(dbt.type + " [");
				for(Description d : dbt.descriptions) {
					System.out.print(d.getDescription() + " ");
				}
				System.out.print("] ");
			}
			System.out.println("}");
		}
	}
}
