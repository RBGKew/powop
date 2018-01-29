package org.powo.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class searchableFilterQuery extends QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		query.removeFilterQuery("base.class_searchable_b:true");
		query.addFilterQuery("base.class_searchable_b:" + value);
	}
}
