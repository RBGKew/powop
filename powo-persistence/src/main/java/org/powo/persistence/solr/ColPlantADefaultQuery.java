package org.powo.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.powo.model.solr.DefaultQueryOption;

import com.google.common.base.Joiner;

public class ColPlantADefaultQuery implements DefaultQueryOption {
	public void add(SolrQuery query){
		String[] sources = {"ColPlantA",};
		String value = Joiner.on(" AND ").join(sources);
		query.addFilterQuery(String.format("%s:\"%s\"", "searchable.sources_ss", value));
	}
}
