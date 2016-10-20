/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.portal.view;

import static org.junit.Assert.assertEquals;

import org.emonocot.model.Description;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;

public class BibliographyTest {

	private Taxon taxon;

	@Before
	public void setUp() {
		this.taxon = new Taxon();
	}

	@Test
	public void testAcceptedFilter() {
		Reference acceptedIn = new Reference();
		acceptedIn.setSubject("Accepted");
		taxon.setReferences(ImmutableSet.<Reference>of(acceptedIn));

		Bibliography bib = new Bibliography(taxon);
		assertEquals(1, bib.getAcceptedIn().size());
		assertEquals(0, bib.getReferences().size());
	}

	@Test
	public void testSynonymFilter() {
		Reference acceptedIn = new Reference();
		acceptedIn.setSubject("Synonym");
		taxon.setReferences(ImmutableSet.<Reference>of(acceptedIn));

		Bibliography bib = new Bibliography(taxon);
		assertEquals(1, bib.getSynonomizedIn().size());
		assertEquals(0, bib.getReferences().size());
	}

	@Test
	public void testCollectingReferencesFromSynonyms() {
		Reference nameReference = new Reference();
		Reference descriptionReference = new Reference();
		Description description = new Description();
		Taxon synonym = new Taxon();

		description.setReferences(ImmutableSet.<Reference>of(descriptionReference));
		synonym.setTaxonomicStatus(TaxonomicStatus.Synonym);
		synonym.setReferences(ImmutableSet.<Reference>of(nameReference));
		synonym.setDescriptions(ImmutableSet.<Description>of(description));
		taxon.setSynonymNameUsages(ImmutableSet.<Taxon>of(synonym));

		Bibliography bib = new Bibliography(taxon);
		assertEquals("accepted name should have description reference from synonym", 1, bib.getReferences().size());
		assertEquals(descriptionReference, Iterators.getOnlyElement(bib.getReferences().iterator()).reference);
	}

	@Test
	public void testIgnoringReferencesInSynonyms() {
		Reference nameReference = new Reference();
		Reference descriptionReference = new Reference();
		Description description = new Description();

		description.setReferences(ImmutableSet.<Reference>of(descriptionReference));
		taxon.setTaxonomicStatus(TaxonomicStatus.Synonym);
		taxon.setReferences(ImmutableSet.<Reference>of(nameReference));
		taxon.setDescriptions(ImmutableSet.<Description>of(description));

		Bibliography bib = new Bibliography(taxon);
		assertEquals("synonym should have reference attached to name", 1, bib.getReferences().size());
		assertEquals(nameReference, Iterators.getOnlyElement(bib.getReferences().iterator()).reference);
	}
}