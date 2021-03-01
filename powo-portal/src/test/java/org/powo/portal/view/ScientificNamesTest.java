package org.powo.portal.view;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.powo.model.Taxon;

import com.google.common.collect.ImmutableList;

public class ScientificNamesTest {

	Collection<Taxon> taxa;
	Taxon family;
	Taxon genus;
	Taxon hybridGenus;
	Taxon species;
	Taxon hybridSpecies;
	Taxon infraspecies;

	@Before
	public void setup() {
		family = new Taxon();
		genus = new Taxon();
		hybridGenus = new Taxon();
		species = new Taxon();
		hybridSpecies = new Taxon();
		infraspecies = new Taxon();

		family.setFamily("Orchidaceae");
		family.setScientificName("Orchidaceae");

		genus.setFamily("Orchidaceae");
		genus.setGenus("Anguloa");
		genus.setScientificName("Anguloa");


		hybridGenus.setFamily("Orchidaceae");
		hybridGenus.setGenus("Aberconwayara");
		hybridGenus.setScientificName("× Aberconwayara");

		species.setFamily("Orchidaceae");
		species.setGenus("Anguloa");
		species.setSpecificEpithet("virginalis");
		species.setScientificName("Anguloa virginalis");

		hybridSpecies.setFamily("Orchidaceae");
		hybridSpecies.setGenus("Anguloa");
		hybridSpecies.setSpecificEpithet("acostae");
		hybridSpecies.setScientificName("Anguloa × acostae");

		infraspecies.setFamily("Orchidaceae");
		infraspecies.setGenus("Anguloa");
		infraspecies.setSpecificEpithet("virginalis");
		infraspecies.setInfraspecificEpithet("turneri");
		infraspecies.setScientificName("Anguloa virginalis var. turneri");

		taxa = ImmutableList.<Taxon>of(genus, infraspecies, family, hybridGenus, species, hybridSpecies);
	}

	@Test
	public void testNameSort() {
		ScientificNames sn = new ScientificNames(taxa);
		List<Taxon> expected = ImmutableList.<Taxon>of(family, hybridGenus, genus, hybridSpecies, species, infraspecies);
		assertEquals(expected, sn.getSorted());
	}

	@Test
	public void testNonHybridCount() {
		ScientificNames sn = new ScientificNames(taxa);
		assertEquals(4, sn.getNonHybridCount());
	}
}
