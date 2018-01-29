package org.powo.persistence.solr;

import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;

import com.google.common.collect.ImmutableSet;

public class AutoCompleteBuilder {

	private SolrQuery query = new SolrQuery().setRequestHandler("/suggest");

	private Integer pageSize = 5;

	private static final Set<String> ranks = ImmutableSet.<String>of("FAMILY","GENUS", "SPECIES");

	private List<String> workingSuggesters;
	
	private boolean suggesterSet = false;

	private void addWorkingSuggester(String suggester) {
		if(workingSuggesters != null && workingSuggesters.contains(suggester)) {
			query.add("suggest.dictionary", suggester);
			suggesterSet = true;
		}
	}

	public AutoCompleteBuilder setWorkingSuggesters(List<String> suggesters) {
		workingSuggesters = suggesters;
		return this;
	}

	public AutoCompleteBuilder addSuggester(String suggester) {
		if(ranks.contains(suggester.toUpperCase())) {
			addWorkingSuggester("scientific-name");
			query.add("suggest.cfq", suggester.toUpperCase());
		} else {
			addWorkingSuggester(suggester);
		}
		return this;
	}

	public AutoCompleteBuilder setSuggesters(List<String> suggesters) {
		for(String suggester : suggesters) {
			addSuggester(suggester);
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

	public SolrQuery build () {
		if (suggesterSet) {
			query.add("suggest.count", pageSize.toString());
			return query;
		}
		return null;
	}
}

