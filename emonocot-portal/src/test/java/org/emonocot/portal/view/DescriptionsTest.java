package org.emonocot.portal.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.emonocot.model.constants.DescriptionType.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.emonocot.model.Description;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.registry.Organisation;
import org.emonocot.portal.view.Descriptions.DescriptionsBySource;
import org.emonocot.portal.view.Descriptions.DescriptionsByType;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterators;

public class DescriptionsTest {

	private Taxon taxon;
	private Description d1;
	private Organisation o1;

	@Before
	public void setUp() {
		taxon = new Taxon();
		d1 = new Description();
		o1 = new Organisation();

		taxon.setScientificName("Eucalyptus");
		o1.setAbbreviation("FWTA");
		o1.setTitle("Flora of West Tropical Africa");
		d1.setAuthority(o1);
	}

	@Test
	public void testBySource() {
		Description d2 = new Description();
		Description d3 = new Description();
		Organisation o2 = new Organisation();

		o2.setAbbreviation("FTEA");
		o2.setTitle("Flora of Tropical East Africa");

		d1.setType(DescriptionType.morphologyGeneralHabit);
		d1.setDescription("Perenial herb");

		d2.setType(DescriptionType.morphologyLeaf);
		d2.setDescription("Basal leaves");
		d2.setAuthority(o1);

		d3.setType(DescriptionType.morphologyReproductiveFlower);
		d3.setDescription("Large");
		d3.setAuthority(o2);

		taxon.setDescriptions(ImmutableSet.<Description>of(d1, d2, d3));

		Descriptions ds = new Descriptions(taxon);

		Collection<DescriptionsBySource> dbs = ds.getBySource();
		assertEquals(2, dbs.size());
		List<String> abbreviations = new ArrayList<>();
		for(DescriptionsBySource d : dbs) {
			abbreviations.add(d.source.getAbbreviation());
		}

		assertThat(abbreviations, containsInAnyOrder("FWTA", "FTEA"));
	}

	@Test
	public void testDescriptionTypeFiltering() {
		Description d2 = new Description();

		d1.setType(useAnimalFoodBees);
		d1.setDescription("BEES!!!");

		d2.setType(useMedicinesDigestiveSystemDisorders);
		d2.setDescription("You have died of dysentery");
		d2.setAuthority(o1);

		taxon.setDescriptions(ImmutableSet.<Description>of(d1, d2));

		Descriptions ds = new Descriptions(taxon, true);
	}

	@Test
	public void testMultiTypeDescriptions() {
		d1.setTypes(ImmutableSortedSet.<DescriptionType>of(useAnimalFoodBees, morphologyReproductiveFlower));
		d1.setDescription("BEES!!!");

		taxon.setDescriptions(ImmutableSet.<Description>of(d1));

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

	@Test
	public void testSynonymDescriptions() {
		Description d2 = new Description();
		Taxon synonym = new Taxon();
		synonym.setScientificName("Eucalypton");
		synonym.setTaxonomicStatus(TaxonomicStatus.Synonym);

		d1.setType(DescriptionType.morphologyGeneral);
		d1.setDescription("accepted description");

		d2.setAuthority(o1);
		d2.setType(DescriptionType.morphologyGeneralHabit);
		d2.setDescription("synonym description");

		synonym.setDescriptions(ImmutableSet.<Description>of(d2));
		taxon.setDescriptions(ImmutableSet.<Description>of(d1));
		taxon.setSynonymNameUsages(ImmutableSet.<Taxon>of(synonym));

		Descriptions ds = new Descriptions(taxon);
		for(DescriptionsBySource dbs : ds.getBySource()) {
			if(dbs.asTaxon.getScientificName().equals("Eucalypton")) {
				assertEquals(d2, dbs.byType.get(0).descriptions.get(0));
			} else {
				assertEquals(d1, dbs.byType.get(0).descriptions.get(0));
			}
		}
	}

	private void print(Descriptions descriptions) {
		for(DescriptionsBySource dbs : descriptions.getBySource()) {
			System.out.print("{" + dbs.source.getTitle() + " as " + dbs.asTaxon.getScientificName() + ": ");
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
