package org.emonocot.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

import com.google.common.base.Joiner;

public class MultiFieldQuery implements QueryOption {

	private String[] searchFields;

	public MultiFieldQuery(String[] fields) {
		this.searchFields = fields;
		for(int i = 0; i < fields.length; i++) {
			searchFields[i] += ":%1$s";
		}
	}

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		if(value != null && !value.isEmpty()){
			String newQuery = String.format(Joiner.on(" OR ").join(searchFields), value.replace(" ", "+"));
			if(query.getQuery() == null) {
				query.setQuery("(" + newQuery + ")");
			} else {
				query.setQuery(query.getQuery() + " AND (" + newQuery + ")");
			}
		}
	}
}