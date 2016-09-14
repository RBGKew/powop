package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class RangeFilterQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		if(value != null && !value.isEmpty()){
			query.addFilterQuery(String.format("%s:[%s]", key, value));
		}
	}
}
