package org.emonocot.persistence.solr;

import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;

import com.google.common.collect.ImmutableSet;

public class AutoCompleteBuilder {

	private SolrQuery query = new SolrQuery().setRequestHandler("/suggest");
	
	private Integer pageSize = 5;
	
	private static final Set<String> ranks = ImmutableSet.<String>builder()
			.add("FAMILY")
			.add("GENUS")
			.add("SPECIES")
			.build();
	
	public AutoCompleteBuilder addSuggester(String suggester){
		if(ranks.contains(suggester.toUpperCase())){
			query.add("suggest.dictionary", "scientific-name");
			query.add("suggest.cfq", suggester.toUpperCase());
		}else{
			query.add("suggest.dictionary", suggester);
		}
		return this;
	}
	
	public AutoCompleteBuilder setQuery(String string) {
		query.setQuery(string);
		return this;
	}

	public AutoCompleteBuilder pageSize(Integer pageSize){
		this.pageSize = pageSize;
		return this;
	}

	public SolrQuery build (){
		query.add("suggest.count", pageSize.toString());
		return query;
	}
}

