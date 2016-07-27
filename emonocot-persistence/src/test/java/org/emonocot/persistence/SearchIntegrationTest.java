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
package org.emonocot.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.emonocot.api.autocomplete.Match;
import org.emonocot.model.SearchableObject;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.Location;
import org.emonocot.pager.Page;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

public class SearchIntegrationTest extends AbstractPersistenceTest {

	private static Logger logger = LoggerFactory.getLogger(SearchIntegrationTest.class);

	@Before
	public final void setUp() throws Exception {
		super.doSetUp();
	}

	@After
	public final void tearDown() throws Exception {
		super.doTearDown();
	}

	@Override
	public final void setUpTestData() {
		Taxon taxon1 = createTaxon("Aus", "1", null, null, "Aaceae", null, null,
				null, null, null, null, new Location[] {}, null);
		createDescription(taxon1, DescriptionType.habitat, "Lorem ipsum", null);
		Taxon taxon2 = createTaxon("Aus bus", "2", taxon1, null, "Aaceae", null,
				null, null, null, null, null,
				new Location[] {Location.AUSTRALASIA,
				Location.BRAZIL, Location.CARIBBEAN }, null);
		Taxon taxon3 = createTaxon("Aus ceus", "3", taxon1, null, null, null,
				null, null, null, null, null,
				new Location[] {Location.NEW_ZEALAND }, null);
		createTaxon("Aus deus", "4", null, taxon2, "Aaceae", null, null, null,
				null, null, null, new Location[] {}, null);
		createTaxon("Aus eus", "5", null, taxon3, null, null, null, null, null,
				null, null, new Location[] {}, null);
		createTaxon("Alania", "urn:kew.org:wcs:taxon:294463", null, null, null, null, null, null, null,
				null, null, new Location[] {Location.NSW}, null);
		createTaxon(null, "6", null, null, null, null, null, null, null,
				null, null, new Location[] {}, null);
	}

	@Test
	public final void testSearch() throws Exception {
		Page<SearchableObject> results = getSearchableObjectDao().search("taxon.scientific_name_t:Aus", null, null, null,
				new String[] {"taxon.distribution_ss" }, null, null, null, null);
		assertEquals("There should be 5 taxa matching Aus", new Integer(5), (Integer)results.getSize());
	}

	@Test
	public final void testNomenclaturalStatus() {
		Taxon taxon = getTaxonDao().find("1");
		assertEquals("The nomenclaturalStatus must be null",null,taxon.getNomenclaturalStatus());
	}

	@Test
	public final void testRestrictedSearch() throws Exception {
		Map<String, String> selectedFacets = new HashMap<String, String>();

		selectedFacets.put("taxon.distribution_ss", Location.AUSTRALASIA.getCode());

		Page<SearchableObject> results = getSearchableObjectDao().search("taxon.scientific_name_t:Aus", null, null, null,
				new String[] {"taxon.distribution_ss"}, null, selectedFacets, null, null);
		assertEquals("There should be 2 taxa matching Aus found in AUSTRALASIA", new Integer(2), (Integer)results.getSize());
		for(String facetName : results.getFacetNames()) {
			logger.debug(facetName);
			FacetField facet = results.getFacetField(facetName);
			for(Count count : facet.getValues()) {
				logger.debug("\t" + count.getName() + " " + count.getCount());
			}
		}
	}

	@Test
	public final void testSearchEmbeddedContent() throws Exception {
		Page<SearchableObject> page = getSearchableObjectDao().search("Lorem", null, null, null, null, null, null, null, null);

		assertFalse(page.getSize() == 0);
	}

	@Test
	public final void testSearchByHigherName() throws Exception {
		Page<SearchableObject> results = searchableObjectDao.search("Aaceae", null, null, null, null, null, null, null, null);

		assertEquals("There should be 3 results", 3, results.getSize().intValue());
	}

	@Test
	public final void testSearchBySynonym() throws Exception {
		Page<SearchableObject> results = searchableObjectDao.search("deus", null, null, null, null, null, null, null, null);

		assertEquals("There should be 2 results, the synonym and accepted name", 2, results.getSize().intValue());
	}

	/**
	 * Test method for {@link org.emonocot.persistence.dao.hibernate.TaxonDaoImpl#findByExample(org.emonocot.model.Taxon)}.
	 */
	@Test
	public void testFindByExample() {
		Taxon example = new Taxon();
		example.setFamily("Aaceae");
		Page<Taxon> results = getTaxonDao().searchByExample(example, false, false);
		assertEquals("There should be 3 results", new Integer(3), results.getSize());

	}

	/**
	 * BUG #308 As an eMonocot user when I search results by A to Z I do not
	 * understand the order of the results page.
	 */
	@Test
	public final void testSearchWithNulls() throws Exception {
		Page<SearchableObject> results = searchableObjectDao.search("", null, null, null, null, null, null, "searchable.label_sort_asc", null);

		assertEquals("There should be 7 results", 7, results.getSize().intValue());

		assertEquals("The first results should be urn:kew.org:wcs:taxon:294463", "urn:kew.org:wcs:taxon:294463", results.getRecords().get(0).getIdentifier());
	}

	/**
	 * Leading whitespace should be trimmed
	 */
	@Test
	public final void testLeadingWhitespace() {
		boolean exceptionThrown = false;
		try {
			Page<SearchableObject> results = getSearchableObjectDao().search(" Aus bus", null, null, null, null, null, null, null, null);
		} catch(Exception e) {
			exceptionThrown = true;
		}
		assertFalse("Leading whitespace should not cause an exception to be thrown", exceptionThrown);
	}

	/**
	 * Autocomplete
	 */
	@Test
	public final void testAutocomplete() throws Exception {
		List<Match> matched = getSearchableObjectDao().autocomplete("Aus bu", 10, null);
	}
}
