package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class PageSizeQuery extends QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		Integer currentRows = query.getRows();
		Integer pageSize = Integer.valueOf(value);
		query.setRows(pageSize);
		if(query.getStart() != null && currentRows != null){
			Integer page = query.getStart() / query.getRows();
			query.setStart(page * pageSize);
		}
	}
}
