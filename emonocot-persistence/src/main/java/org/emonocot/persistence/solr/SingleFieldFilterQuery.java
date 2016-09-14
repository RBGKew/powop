package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class SingleFieldFilterQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		value = value.replace(" ", "+");
		if(query.getQuery() == null) {
			query.setQuery(String.format("%s:%s", key, value));
		} else {
			query.setQuery(String.format("%s AND %s:%s", query.getQuery(), key, value));
		}
	}
}
