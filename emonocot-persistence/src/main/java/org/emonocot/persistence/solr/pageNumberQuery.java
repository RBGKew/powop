package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class pageNumberQuery implements QueryOption {

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		Integer start = 0;
		if(query.getRows() != null){
			start = Integer.valueOf(value) * query.getRows();
		}else{
			start = Integer.valueOf(value) * 24;
		}
		
		query.setStart(start);
	}
}
