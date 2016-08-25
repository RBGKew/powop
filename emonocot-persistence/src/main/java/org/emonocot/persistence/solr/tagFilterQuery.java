package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class tagFilterQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {

		query.addFilterQuery(String.format("{!tag=%s1}%s1:%s", key, value));
	}
}
