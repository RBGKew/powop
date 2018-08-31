package org.powo.portal.json;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class SearchResponse {
	private Integer totalResults;
	private Integer page;
	private Integer totalPages;
	private Integer perPage;
	private String sort;
	private String cursor;
	@Singular
	private List<SearchResult> results;
}
