package org.powo.persistence.solr;

import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;

public class AutoCompleteBuilder {

	private SolrQuery query = new SolrQuery().setRequestHandler("/suggest");

	private Integer pageSize = 5;
	
	private String filterQuery;
	
	private boolean suggesterSet = false;

	public AutoCompleteBuilder setSuggesters(List<String> suggesters) {
		for(String suggester : suggesters) {
			query.add("suggest.dictionary", suggester);
			suggesterSet = true;
		}
		return this;
	}

	public AutoCompleteBuilder setQuery(String string) {
		query.setQuery(string);
		return this;
	}

	public AutoCompleteBuilder pageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}
	
	public AutoCompleteBuilder setFilterQuery(String filterQuery) {
		this.filterQuery = filterQuery;
		return this;
	}

	public SolrQuery build () {
		if (suggesterSet) {
			query.add("suggest.count", pageSize.toString());
			if(filterQuery != null){
				query.add("suggest.cfq", filterQuery);
			}
			return query;
		}
		return null;
	}
}

