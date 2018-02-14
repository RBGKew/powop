package org.powo.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.powo.model.solr.DefaultQueryOption;

import com.google.common.base.Joiner;

public class PubDefaultQuery implements DefaultQueryOption {
	public void add(SolrQuery query){
		String[] sources = {"pub",};
		String value = Joiner.on(" AND ").join(sources);
		query.addFilterQuery(String.format("%s:\"%s\"", "searchable.sources_ss", value));
	}
}
