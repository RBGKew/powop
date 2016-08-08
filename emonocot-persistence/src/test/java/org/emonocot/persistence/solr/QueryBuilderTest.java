package org.emonocot.persistence.solr;

import java.util.List;
import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Test;

public class QueryBuilderTest {

	@Test
	public void BasicFilterQuery(){
		QueryBuilder querybuilder = new QueryBuilder();
		SolrQuery query = querybuilder.addParam("test", "blarg").build();
		List<String> filterQueries = Arrays.asList(query.getFilterQueries());
		//check the query contains the filter we just put in
		assertTrue(filterQueries.contains("test:blarg"));
		//check the query contains only those facets
		assertTrue(filterQueries.size() == 2);
		System.out.println(query.toQueryString());
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
		List<String> filterQueries = Arrays.asList(query.getFilterQueries());
		assertTrue(filterQueries.contains("taxon.family_ss:blarg OR taxon.genus_ss:blarg OR taxon.species_ss:blarg OR taxon.vernacular_names_ss:blarg OR taxon.description_t:blarg"
				+ " OR taxon.distribution_ss:blarg OR taxon.name_published_in_string_s:blarg OR taxon.scientific_name_authorship_s:blarg"));

	}
}
