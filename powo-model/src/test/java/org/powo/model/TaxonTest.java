package org.powo.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Sets;

import org.powo.model.constants.TaxonomicStatus;
import org.powo.model.registry.Organisation;
import org.junit.Test;

public class TaxonTest {

	TaxonomicStatus[] synonyms = new TaxonomicStatus[] {
			TaxonomicStatus.Synonym,
			TaxonomicStatus.Heterotypic_Synonym,
			TaxonomicStatus.Homotypic_Synonym,
			TaxonomicStatus.IntermediateRankSynonym,
			TaxonomicStatus.Proparte_Synonym,
			TaxonomicStatus.DeterminationSynonym};

	TaxonomicStatus[] appearsAccepted = new TaxonomicStatus[] {
			TaxonomicStatus.Accepted,
			TaxonomicStatus.Doubtful,
			TaxonomicStatus.Artificial_Hybrid,
			TaxonomicStatus.Unplaced,
			TaxonomicStatus.PartiallyAccepted,
		};

	@Test
	public void testIsTaxonSynonym() {
		Taxon taxon = new Taxon();
		assertFalse("Taxon with blank taxonomic status should not be a synonym", taxon.isSynonym());
		List<TaxonomicStatus> synonymList = Arrays.asList(synonyms);
		for (TaxonomicStatus status : TaxonomicStatus.values()) {
			taxon.setTaxonomicStatus(status);
			if (synonymList.contains(status)) {
				assertTrue("Taxon with " + status.toString() + " status should be a synonym", taxon.isSynonym());
			} else {
				assertFalse("Taxon with " + status.toString() + " status should not be a synonym", taxon.isSynonym());
			}
		}
	}

	@Test
	public void testIsTaxonAccepted() {
		Taxon taxon = new Taxon();
		assertFalse("Taxon with blank taxonomic status should not be accepted", taxon.isAccepted());
		for (TaxonomicStatus status : TaxonomicStatus.values()) {
			taxon.setTaxonomicStatus(status);
			if (status == TaxonomicStatus.Accepted || status == TaxonomicStatus.PartiallyAccepted) {
				assertTrue("Taxon with " + status.toString() + " status should be accepted", taxon.isAccepted());
			} else {
				assertFalse("Taxon with " + status.toString() + " status should not be accepted", taxon.isAccepted());
			}
		}
	}

	@Test
	public void testDoesTaxonLookAccepted() {
		Taxon taxon = new Taxon();
		assertTrue("Taxon with blank taxonomic status should look accepted", taxon.looksAccepted());
		List<TaxonomicStatus> acceptedList = Arrays.asList(appearsAccepted);
		for (TaxonomicStatus status : TaxonomicStatus.values()) {
			taxon.setTaxonomicStatus(status);
			if (acceptedList.contains(status)) {
				assertTrue("Taxon with " + status.toString() + " status should appear accepted", taxon.looksAccepted());
			} else {
				assertFalse("Taxon with " + status.toString() + " status should not appear accepted", taxon.looksAccepted());
			}
		}
	}

	@Test
	public void testAddAuthorityToTaxonAndRelatedTaxa() {
		var authority = new Organisation();

		var taxon = new Taxon();
		taxon.addAuthorityToTaxonAndRelatedTaxa(authority);

		assertEquals(Sets.newHashSet(authority), taxon.getAuthorities());
	}

	@Test
	public void testAddAuthorityToTaxonAndRelatedTaxaAccepted() {
		var authority = new Organisation();

		var parent = new Taxon();
		var synonym = new Taxon();

		var accepted = new Taxon();
		accepted.setTaxonomicStatus(TaxonomicStatus.Accepted);
		accepted.setSynonymNameUsages(Sets.newHashSet(synonym));
		synonym.setAcceptedNameUsage(accepted);
		accepted.setParentNameUsage(parent);
		accepted.addAuthorityToTaxonAndRelatedTaxa(authority);

		assertEquals(Sets.newHashSet(authority), accepted.getAuthorities());
		assertEquals(Sets.newHashSet(authority), accepted.getCombinedAuthorities());
		assertEquals(Sets.newHashSet(), synonym.getAuthorities());
		assertEquals(Sets.newHashSet(authority), synonym.getCombinedAuthorities());
		assertEquals(Sets.newHashSet(authority), parent.getAuthorities());

	}

	@Test
	public void testAddAuthorityToTaxonAndRelatedTaxaSynonym() {
		var authority = new Organisation();

		var accepted = new Taxon();
		accepted.setTaxonomicStatus(TaxonomicStatus.Accepted);
		var acceptedParent = new Taxon();
		accepted.setParentNameUsage(acceptedParent);

		var synonymParent = new Taxon();

		var synonym = new Taxon();
		synonym.setAcceptedNameUsage(accepted);
		accepted.setSynonymNameUsages(Sets.newHashSet(synonym));
		synonym.setParentNameUsage(synonymParent);
		synonym.addAuthorityToTaxonAndRelatedTaxa(authority);

		assertEquals(Sets.newHashSet(authority), synonym.getAuthorities());
		assertEquals(Sets.newHashSet(authority), synonym.getCombinedAuthorities());
		assertEquals(Sets.newHashSet(authority), accepted.getAuthorities());
		assertEquals(Sets.newHashSet(authority), accepted.getCombinedAuthorities());
		assertEquals(Sets.newHashSet(authority), acceptedParent.getAuthorities());
		assertEquals(Sets.newHashSet(), synonymParent.getAuthorities());
	}

	@Test
	public void testAddAuthorityToTaxonAndRelatedTaxaRecursive() {
		var authority = new Organisation();

		var species = new Taxon();
		species.setTaxonomicStatus(TaxonomicStatus.Accepted);
		var genus = new Taxon();
		genus.setTaxonomicStatus(TaxonomicStatus.Accepted);
		var family = new Taxon();
		
		species.setParentNameUsage(genus);
		genus.setParentNameUsage(family);

		species.addAuthorityToTaxonAndRelatedTaxa(authority);

		assertEquals(Sets.newHashSet(authority), species.getAuthorities());
		assertEquals(Sets.newHashSet(authority), genus.getAuthorities());
		assertEquals(Sets.newHashSet(authority), family.getAuthorities());
	}
}