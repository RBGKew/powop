package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class PageNumberQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		int rows = query.getRows() == null ? 24 : query.getRows();
		int start = Integer.valueOf(value) * rows;

		query.setStart(start);
	}
}
