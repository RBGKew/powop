package org.powo.portal.json;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@XmlRootElement
@JsonInclude(Include.NON_EMPTY)
public class SearchResponse {
	private Integer totalResults;
	private Integer page;
	private Integer totalPages;
	private Integer perPage;
	private String sort;
	private String cursor;
	private String message;
	@Singular
	private List<SearchResult> results;
}
