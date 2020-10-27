package org.powo.portal.controller;

import org.junit.Test;
import org.powo.portal.json.SearchResponse;
import org.powo.portal.json.SearchResponse.SearchResponseBuilder;
import org.powo.portal.json.SearchResult;
import org.powo.portal.json.SearchResult.SearchResultBuilder;

public class JsonBuilderTest {

	@Test
	public void resultBuilderTest() {
		SearchResultBuilder resultBuilder = SearchResult.builder();
		resultBuilder.url("http://fakeurl.org/10");
		resultBuilder.author("Blaaaarg");
		resultBuilder.rank("Family");
		resultBuilder.name("Blaaaargacea");
	}

	@Test
	public void resultBuilderWithImagesTest() {
		SearchResultBuilder resultBuilder = SearchResult.builder();
		resultBuilder.url("http://fakeurl.org/100");
		resultBuilder.author("Blaaaarg");
		resultBuilder.rank("Species");
		resultBuilder.name("Blaaaarga blaaarg");
		resultBuilder.addImage("http://fakeurl.org/image/1000_thumb.jpg", "http://fakeurl.org/image/1000_fullsize.jpg", "bear eating plant");
	}

	@Test
	public void mainBuilderTest() {
		SearchResultBuilder resultBuilder = SearchResult.builder();
		resultBuilder.url("http://fakeurl.org/100");
		resultBuilder.author("Blaaaarg");
		resultBuilder.rank("Species");
		resultBuilder.name("Blaaaarga blaaarg");
		resultBuilder.addImage("http://fakeurl.org/image/1000_thumb.jpg", "http://fakeurl.org/image/1000_fullsize.jpg", "bear eating plant");

		SearchResponseBuilder mainJsonBuilder = SearchResponse.builder();
		mainJsonBuilder.result(resultBuilder.build());
		mainJsonBuilder.sort("relevance_asc");
		mainJsonBuilder.page(0);
		mainJsonBuilder.perPage(10);
		mainJsonBuilder.totalResults(1000);
	}
}
