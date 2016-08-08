package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class searchableFilterQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		query.removeFilterQuery("base.class_searchable_b:true");
		query.addFilterQuery("base.class_searchable_b:" + value);
	}
}
