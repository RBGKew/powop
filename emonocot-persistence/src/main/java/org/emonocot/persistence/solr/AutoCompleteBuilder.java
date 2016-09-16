package org.emonocot.persistence.solr;

import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;

import com.google.common.collect.ImmutableSet;

public class AutoCompleteBuilder {

	private SolrQuery query = new SolrQuery().setRequestHandler("/suggest");
	
	private Integer pageSize = 5;
	
	private Boolean suggesterSet = false; 
	
	private static final Set<String> ranks = ImmutableSet.<String>builder()
			.add("FAMILY")
			.add("GENUS")
			.add("SPECIES")
			.build();
	
	private List<String> workingSuggesters;
	
	private void addWorkingSuggester(String suggester){
		if(workingSuggesters != null && workingSuggesters.contains(suggester)){
			query.add("suggest.dictionary", suggester);
			suggesterSet = true;
		}		
	}
	
	public AutoCompleteBuilder setWorkingSuggesters(List<String> suggesters){
		workingSuggesters = suggesters;
		return this;	
	}
	
	public AutoCompleteBuilder addSuggester(String suggester){
		if(ranks.contains(suggester.toUpperCase())){
			addWorkingSuggester("scientific-name");
			query.add("suggest.cfq", suggester.toUpperCase());
		}else{
			addWorkingSuggester(suggester);
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
		if(suggesterSet){
			query.add("suggest.count", pageSize.toString());
			return query;
		}
		return null;
	}
}

