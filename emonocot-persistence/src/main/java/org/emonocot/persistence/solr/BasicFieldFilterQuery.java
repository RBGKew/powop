package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class BasicFieldFilterQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {

		query.addFilterQuery(String.format("%s:%s", key, value));
	}
}
