package org.powo.portal.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@JsonInclude(Include.NON_EMPTY)
@Data
@Builder
public class SearchResult {

	private boolean accepted;
	private String author;
	private String kingdom;
	private String family;
	private String name;
	private String rank;
	private String snippet;
	private SearchResult synonymOf;
	private String url;
	private String fqId;
	private List<Map<String, String>> images;

	public static class SearchResultBuilder {
		public SearchResultBuilder addImage(String thumb, String fullsize, String caption) {
			if (images == null) {
				images = new ArrayList<>();
			}

			Map<String, String> image = new HashMap<String, String>();
			image.put("thumbnail", thumb);
			image.put("fullsize", fullsize);
			image.put("caption", caption);
			images.add(image);
			return this;
		}
	}
}
