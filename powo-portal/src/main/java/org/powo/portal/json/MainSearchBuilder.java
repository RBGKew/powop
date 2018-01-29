package org.powo.portal.json;

import java.util.ArrayList;
import java.util.List;

public class MainSearchBuilder {

	private Integer totalResults;
	private Integer page;
	private Integer totalPages;
	private Integer perPage;
	private String sort;
	private List<SearchResultBuilder> results = new ArrayList<SearchResultBuilder>();

	public MainSearchBuilder totalResults(Integer totalResults){
		this.totalResults = totalResults;
		return this;
	}

	public MainSearchBuilder page(Integer page){
		this.page = page;
		return this;
	}

	public MainSearchBuilder perPage(Integer perPage){
		this.perPage = perPage;
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

	public List<SearchResultBuilder>getResults(){
		return results;
	}


}
