package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class pageNumberQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		Integer start = Integer.valueOf(value) * query.getRows();
		query.setStart(start);
	}
}
