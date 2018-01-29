package org.powo.persistence.solr;

import org.apache.solr.client.solrj.SolrQuery;

public abstract class QueryOption {

	protected String prepareValue(String field, String value) {
		if(field.endsWith("_s") || field.endsWith("_s_lower") || field.endsWith("_ss_lower")) {
			// string fields should be quoted to exactly match multi-word values
			return String.format("\"%s\"", value);
		} else if (field.endsWith("_t")){
			// general text fields are quoted with a phrase slop 
			return String.format("\"%s\"~10", value);
		} else {
			// otherwise, encode spaces
			return value.replace(" ", "+");
		}
	}

	public abstract void addQueryOption(String key, String value, SolrQuery query);
}
