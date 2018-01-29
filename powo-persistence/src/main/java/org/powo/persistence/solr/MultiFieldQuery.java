package org.powo.persistence.solr;

import java.util.ArrayList;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

public class MultiFieldQuery extends QueryOption {

	private static final Logger logger = LoggerFactory.getLogger(MultiFieldQuery.class);
	private ArrayList<String> searchFields;

	public MultiFieldQuery(Set<String> fields) {
		this.searchFields = new ArrayList<String>(fields.size());
		for(String field : fields) {
			searchFields.add(String.format("%s:%s", field, prepareValue(field, "%1$s")));
		}
	}

	@Override
	public void addQueryOption(String key, String value, SolrQuery query) {
		if(value != null && !value.isEmpty()){
			String newQuery = String.format(Joiner.on(" OR ").join(searchFields), value);
			if(query.getQuery() == null) {
				query.setQuery("(" + newQuery + ")");
			} else {
				query.setQuery(query.getQuery() + " AND (" + newQuery + ")");
			}
		}
	}
}