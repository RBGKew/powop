package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class pageSizeQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		Integer page = query.getStart() / query.getRows();
		Integer pageSize = Integer.valueOf(value);
		query.setRows(pageSize);
		query.setStart(page * pageSize);
	}
}
