package org.emonocot.portal.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_EMPTY)
public class SearchResultBuilder {

	private boolean accepted;
	private String author; 
	private String kingdom;
	private String name;
	private String rank;
	private String snippet;
	private SearchResultBuilder synonymOf;
	private String url;
	private List<Map<String, String>> images = new ArrayList<Map<String, String>>();

	public SearchResultBuilder accepted(boolean accepted) {
		this.accepted = accepted;
		return this;
	}

	public SearchResultBuilder url(String url) {
		this.url = url;
		return this;
	}

	public SearchResultBuilder name(String name) {
		this.name = name;
		return this;
	}

	public SearchResultBuilder author(String author) {
		this.author = author;
		return this;
	}

	public SearchResultBuilder rank(String rank) {
		this.rank = rank;
		return this;
	}

	public SearchResultBuilder snippet(String string) {
		this.snippet = string;
		return this;
	}

	public SearchResultBuilder kingdom(String kingdom) {
		this.kingdom = kingdom;
		return this;
	}

	public SearchResultBuilder synonymOf(SearchResultBuilder accepted) {
		this.synonymOf = accepted;
		return this;
	}

	public SearchResultBuilder addImage(String thumb, String fullsize, String caption) {
		Map<String, String> image = new HashMap<String, String>();
		image.put("thumbnail", thumb);
		image.put("fullsize", fullsize);
		image.put("caption", caption);
		images.add(image);
		return this;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}
	public String getAuthor() {
		return author;
	}

	public String getRank() {
		return rank;
	}

	public String getSnippet() {
		return snippet;
	}

	public String getKingdom() {
		return kingdom;
	}

	public SearchResultBuilder getSynonymOf() {
		return synonymOf;
	}

	public List<Map<String, String>> getImages() {
		return images;
	}
}
