package org.emonocot.portal.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchResultBuilder {

	private String name;
	private String url;
	private String author; 
	private String rank;
	private Map<String, List<String>> snippet;
	private List<Map<String, String>> images = new ArrayList<Map<String, String>>();

	public SearchResultBuilder url(String url){
		this.url = url;
		return this;
	}

	public SearchResultBuilder name(String name){
		this.name = name;
		return this;
	}

	public SearchResultBuilder author(String author){
		this.author = author;
		return this;
	}

	public SearchResultBuilder rank(String rank){
		this.rank = rank;
		return this;
	}

	public SearchResultBuilder snippet(Map<String, List<String>> map){
		this.snippet = map;
		return this;
	}

	public SearchResultBuilder addImage(String url, String caption){
		Map<String, String> image = new HashMap<String, String>();
		image.put("url", url);
		image.put("caption", caption);
		images.add(image);
		return this;
	}

	public String getName(){
		return name;
	}

	public String getUrl(){
		return url;
	}
	public String getAuthor(){
		return author;
	}

	public String getRank(){
		return rank;
	}

	public Map<String, List<String>> getSnippet(){
		return snippet;
	}

	public List<Map<String, String>> getImages(){
		return images;
	}

}
