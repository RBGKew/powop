package org.powo.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.powo.model.solr.DefaultQueryOption;

/**
 * This class allows arbitrary filters to be added to a query. It is used by ColPlantASite
 * to create a filter that includes taxa that have the ColPlantA organisation OR are plants found in Colombia.
 */
public class CustomDefaultQuery implements DefaultQueryOption {

	private String customQuery;

	public CustomDefaultQuery(String customQuery) {
		this.customQuery = customQuery;
	}

	@Override
	public void add(SolrQuery query) {
		query.addFilterQuery(customQuery);
	};
}
