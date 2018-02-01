package org.powo.model.solr;

import org.apache.solr.client.solrj.SolrQuery;

public interface DefaultQueryOption {
	
	public void add(SolrQuery query);
}
