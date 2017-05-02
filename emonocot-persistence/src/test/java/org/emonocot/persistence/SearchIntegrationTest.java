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

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.Location;
import org.emonocot.pager.Page;
import org.emonocot.persistence.solr.AutoCompleteBuilder;
import org.emonocot.persistence.solr.QueryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		Taxon taxon1 = createTaxon("Aus", "1", null, null, "Aaceae", null, null, null, null, null, null, new Location[] {}, null); createDescription(taxon1, DescriptionType.habitat, "Lorem ipsum", null);
		Taxon taxon2 = createTaxon("Aus bus", "2", taxon1, null, "Aaceae", null, null, null, null, null, null, new Location[] {Location.AUSTRALASIA, Location.BRAZIL, Location.CARIBBEAN }, null);
		Taxon taxon3 = createTaxon("Aus ceus", "3", taxon1, null, "Aaceae", null, null, null, null, null, null, new Location[] {Location.NEW_ZEALAND }, null);
		createTaxon("Aus deus", "4", null, taxon2, "Aaceae", null, null, null, null, null, null, new Location[] {}, null);
		createTaxon("Aus eus", "5", null, taxon3, null, null, null, null, null, null, null, new Location[] {}, null);
		createTaxon("Alania", "urn:kew.org:wcs:taxon:294463", null, null, null, null, null, null, null, null, null, new Location[] {Location.NSW}, null);
		createTaxon(null, "6", null, null, null, null, null, null, null, null, null, new Location[] {}, null);
	}

	@Test
	public final void testSearch() throws Exception {
		SolrQuery query = new QueryBuilder().addParam("any", "Aus").build();
		QueryResponse results = getSearchableObjectDao().search(query);
		assertEquals("There should be 1 taxa matching Aus", 1, results.getResults().size());
	}

	@Test
	public final void testRestrictedSearch() throws Exception {
		SolrQuery query = new QueryBuilder()
				.addParam("location", Location.AUSTRALASIA.getName())
				.addParam("family", "Aaceae").build();

		QueryResponse results = getSearchableObjectDao().search(query);
		assertEquals("There should be 2 taxa matching Aaceae found in AUSTRALASIA", 2, results.getResults().size());
	}

	@Test
	public final void testSearchByHigherName() throws Exception {
		SolrQuery query = new QueryBuilder().addParam("family", "Aaceae").build();
		QueryResponse results = searchableObjectDao.search(query);

		assertEquals("There should be 4 results", 4, results.getResults().size());
	}

	@Test
	public final void testSearchBySynonym() throws Exception {
		SolrQuery query = new QueryBuilder().addParam("any", "Aus deus").build();
		QueryResponse results = searchableObjectDao.search(query);
		assertEquals("There should be 2 results, the synonym and accepted name", 2, results.getResults().size());
	}

	/**
	 * Test method for {@link org.emonocot.persistence.dao.hibernate.TaxonDaoImpl#findByExample(org.emonocot.model.Taxon)}.
	 */
	@Test
	public void testFindByExample() {
		Taxon example = new Taxon();
		example.setFamily("Aaceae");
		Page<Taxon> results = getTaxonDao().searchByExample(example, false, false);
		assertEquals("There should be 4 results", new Integer(4), results.getSize());
	}

	/**
	 * BUG #308 As an eMonocot user when I search results by A to Z I do not
	 * understand the order of the results page.
	 */
	@Test
	public final void testSearchWithNulls() throws Exception {
		SolrQuery query = new QueryBuilder().addParam("any", "").build();
		QueryResponse results = searchableObjectDao.search(query);

		assertEquals("There should be 7 results", 7, results.getResults().size());

		//assertEquals("The first results should be 1", "1", results.getRecords().get(0).getIdentifier());
	}

	/**
	 * Leading whitespace should be trimmed
	 */
	@Test
	public final void testLeadingWhitespace() {
		boolean exceptionThrown = false;
		SolrQuery query = new QueryBuilder().addParam("any", "              Aus bus").build();
		try {
			getSearchableObjectDao().search(query);
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
		SolrQuery query = new AutoCompleteBuilder().setQuery("Africa").pageSize(10).addSuggester("location").build();
		SuggesterResponse matched = getSearchableObjectDao().autocomplete(query);
	}
}
