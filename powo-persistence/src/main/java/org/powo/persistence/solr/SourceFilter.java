package org.powo.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.powo.model.solr.DefaultQueryOption;

public class SourceFilter implements DefaultQueryOption {

	private String source;

	public SourceFilter(String source) {
		this.source = source;
	}

	@Override
	public void add(SolrQuery query) {
		query.addFilterQuery("searchable.sources_ss:" + source);
	}
}
