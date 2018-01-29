package org.powo.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class RangeFilterQuery extends BaseQueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		if(value != null && !value.isEmpty()){
			query.addFilterQuery(String.format("%s:[%s]", key, value));
		}
	}
}
