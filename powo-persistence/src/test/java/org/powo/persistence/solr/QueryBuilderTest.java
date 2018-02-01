package org.powo.persistence.solr;

import java.util.List;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Test;
import org.powo.persistence.solr.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryBuilderTest {

	private static Logger logger = LoggerFactory.getLogger(QueryBuilderTest.class);
	
	@Test
	public void BasicFilterQuery() {
		QueryBuilder queryBuilder = new QueryBuilder().addParam("test", "blarg");
		assertEquals("test:blarg", queryBuilder.build().getQuery());
		queryBuilder.addParam("anotherParam", "blarg");
		assertEquals("test:blarg AND anotherParam:blarg", queryBuilder.build().getQuery());
	}

	@Test
	public void RangeFacet() {
		QueryBuilder querybuilder = new QueryBuilder();
		SolrQuery query = querybuilder.addParam("published", "blarg TO blarg").build();
		List<String> filterQueries = Arrays.asList(query.getFilterQueries());
		assertTrue(filterQueries.contains("taxon.name_published_in_year_i:[blarg TO blarg]"));
	}

	@Test
	public void MainFilterQuery() {
		QueryBuilder querybuilder = new QueryBuilder();
		SolrQuery query = querybuilder.addParam("q", "blarg").build();
		String[] expectedTerms = {
				"taxon.scientific_name_s_lower:\"blarg\"",
				"taxon.family_s_lower:\"blarg\"",
				"taxon.genus_s_lower:\"blarg\"",
				"taxon.species_s_lower:\"blarg\"",
				"taxon.vernacular_names_t:\"blarg\"~10",
				"taxon.name_published_in_s_lower:\"blarg\"",
				"taxon.scientific_name_authorship_s_lower:\"blarg\"",
				"taxon.description_appearance_t:\"blarg\"~10",
				"taxon.description_inflorescence_t:\"blarg\"~10",
				"taxon.description_fruit_t:\"blarg\"~10",
				"taxon.description_leaves_t:\"blarg\"~10",
				"taxon.description_flower_t:\"blarg\"~10",
				"taxon.description_seed_t:\"blarg\"~10",
				"taxon.description_vegitativePropagation_t:\"blarg\"~10",
				"taxon.distribution_ss_lower:\"blarg\""};

		for(String term : expectedTerms) {
			assertThat(query.getQuery(), containsString(term));
		}
	}

	@Test
	public void compoundQuery() {
		SolrQuery q = new QueryBuilder().addParam("q", "leaf:pinnately compound,location:africa,blarg").build();
		assertThat(q.getQuery(), containsString("taxon.description_leaf_t:\"pinnately compound\"~10"));
		assertThat(q.getQuery(), containsString("taxon.distribution_ss_lower:\"africa\""));
		assertThat(q.getQuery(), containsString("taxon.scientific_name_s_lower:\"blarg\""));
	}
}
