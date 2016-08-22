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

import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.emonocot.model.SearchableObject;
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
		SolrQuery query = new QueryBuilder().addParam("main.query", "Aus").build();
		Page<SearchableObject> results = getSearchableObjectDao().search(query, null);		
		assertEquals("There should be 5 taxa matching Aus", new Integer(5), (Integer)results.getSize());
	}

	@Test
	public final void testNomenclaturalStatus() {
		Taxon taxon = getTaxonDao().find("1");
		assertEquals("The nomenclaturalStatus must be null",null,taxon.getNomenclaturalStatus());
	}

	@Test
	public final void testFacetsReturned() throws SolrServerException, IOException{
		SolrQuery query = new QueryBuilder().addParam("main.query", "Aus").build();
		Page<SearchableObject> results = getSearchableObjectDao().search(query, null);
		Map<String, Integer> facetNames = results.getFacetQuerys();
		assertEquals("The Facet Count for accepted names should be 2", new Integer(2), facetNames.get("taxon.taxonomic_status_s:Accepted"));
		assertEquals("The Facet Count for images should be 0", new Integer(0), facetNames.get("taxon.images_not_empty_b:true"));
	}
	
	@Test
	public final void testRestrictedSearch() throws Exception {
		SolrQuery query = new QueryBuilder().addParam("taxon.distribution_ss", Location.AUSTRALASIA.getCode()).addParam("taxon.scientific_name_t", "Aus").build();
		
		Page<SearchableObject> results = getSearchableObjectDao().search(query, null);
		assertEquals("There should be 2 taxa matching Aus found in AUSTRALASIA", new Integer(2), (Integer)results.getSize());
	}

	@Test
	public final void testSearchEmbeddedContent() throws Exception {
		SolrQuery query = new QueryBuilder().addParam("main.query", "Lorem").build();
		Page<SearchableObject> page = getSearchableObjectDao().search(query, null);

		assertFalse(page.getSize() == 0);
	}

	@Test
	public final void testSearchByHigherName() throws Exception {
		SolrQuery query = new QueryBuilder().addParam("taxon.family_ss", "Aaceae").build();
		Page<SearchableObject> results = searchableObjectDao.search(query, null);

		assertEquals("There should be 3 results", 3, results.getSize().intValue());
	}

	@Test
	public final void testSearchBySynonym() throws Exception {
		SolrQuery query = new QueryBuilder().addParam("main.query", "deus").build();
		Page<SearchableObject> results = searchableObjectDao.search(query, null);
		assertEquals("The first result ID should be 2", "2", results.getRecords().get(0).getIdentifier());	
		assertEquals("There should be 2 results, the synonym and accepted name", new Integer(2), results.getSize());
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
		SolrQuery query = new QueryBuilder().addParam("main.query", "").build();
		Page<SearchableObject> results = searchableObjectDao.search(query, null);

		assertEquals("There should be 7 results", 7, results.getSize().intValue());

		assertEquals("The first results should be 1", "1", results.getRecords().get(0).getIdentifier());
	}

	/**
	 * Leading whitespace should be trimmed
	 */
	@Test
	public final void testLeadingWhitespace() {
		boolean exceptionThrown = false;
		SolrQuery query = new QueryBuilder().addParam("main.query", " Aus bus").build();
		try {
			Page<SearchableObject> results = getSearchableObjectDao().search(query, null);
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
		SolrQuery query = new AutoCompleteBuilder().setQuery("Aus bu").pageSize(10).addSuggester("scientific-name").build();
		SuggesterResponse matched = getSearchableObjectDao().autocomplete(query);
	}
}
