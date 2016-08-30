package org.emonocot.portal.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainSearchBuilder {

	private Integer totalResults;
	private Integer page;
	private Integer totalPages;
	private Integer perPage;
	private String sort;
	private String selectedFacet;
	private Map<String, Integer>facets = new HashMap<String, Integer>();
	private List<SearchResultBuilder> results = new ArrayList<SearchResultBuilder>();

	public MainSearchBuilder totalResults(Integer total_results){
		this.totalResults = total_results;
		return this;
	}

	public MainSearchBuilder page(Integer page){
		this.page = page;
		return this;
	}

	public MainSearchBuilder per_page(Integer per_page){
		this.perPage = per_page;
		return this;
	}

	public MainSearchBuilder totalPages(Integer totalPages){
		this.totalPages = totalPages;
		return this;
	}
	
	public MainSearchBuilder sort(String sort){
		this.sort = sort;
		return this;
	}

	public MainSearchBuilder selectedFacet(String selectedFacet){
		this.selectedFacet = selectedFacet;
		return this;
	}
	
	
	public MainSearchBuilder addFacet(String facet, Integer count){
		facets.put(facet, count);
		return this;
	}

	public MainSearchBuilder addResult(SearchResultBuilder result){
		results.add(result);
		return this;
	}

	public Integer getTotalResults(){
		return totalResults;
	}

	public Integer getTotalPages(){
		return totalPages;
	}
	
	public Integer getPage(){
		return page;
	}

	public Integer getPerPage(){
		return perPage;	
	}

	public String getSort(){
		return sort;
	}

	public String getSelectedFacet(){
		return selectedFacet;
	}
	
	public Map<String, Integer>getFacets(){
		return facets;
	}

	public List<SearchResultBuilder>getResults(){
		return results;
	}
	

}
