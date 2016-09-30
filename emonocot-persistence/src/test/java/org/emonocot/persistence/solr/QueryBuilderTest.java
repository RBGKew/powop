package org.emonocot.persistence.solr;

import java.util.List;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryBuilderTest {

	private static Logger logger = LoggerFactory.getLogger(QueryBuilderTest.class);
	
	@Test
	public void BasicFilterQuery(){
		QueryBuilder queryBuilder = new QueryBuilder().addParam("test", "blarg");
		assertEquals("test:blarg", queryBuilder.build().getQuery());
		queryBuilder.addParam("anotherParam", "blarg");
		assertEquals("test:blarg AND anotherParam:blarg", queryBuilder.build().getQuery());
	}
	
	@Test
	public void RangeFacet(){
		QueryBuilder querybuilder = new QueryBuilder();
		SolrQuery query = querybuilder.addParam("taxon.name_published_in_year_i", "blarg TO blarg").build();
		List<String> filterQueries = Arrays.asList(query.getFilterQueries());
		assertTrue(filterQueries.contains("taxon.name_published_in_year_i:[blarg TO blarg]"));
	}

	@Test
	public void MainFilterQuery(){
		QueryBuilder querybuilder = new QueryBuilder();
		SolrQuery query = querybuilder.addParam("main.query", "blarg").build();
		String[] expectedTerms = {
				"taxon.scientific_name_t:blarg",
				"taxon.family_t:blarg",
				"taxon.genus_t:blarg",
				"taxon.species_t:blarg",
				"taxon.vernacular_names_t:blarg",
				"taxon.name_published_in_string_s:blarg",
				"taxon.scientific_name_authorship_t:blarg",
				"taxon.description_appearance_t:blarg",
				"taxon.description_inflorescence_t:blarg",
				"taxon.description_fruit_t:blarg",
				"taxon.description_leaves_t:blarg",
				"taxon.description_flower_t:blarg",
				"taxon.description_seed_t:blarg",
				"taxon.description_vegitativePropagation_t:blarg",
				"taxon.distribution_t:blarg"};

		for(String term : expectedTerms) {
			assertThat(query.getQuery(), containsString(term));
		}
	}
}
