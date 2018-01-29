package org.powo.portal.controller;

import org.junit.Test;
import org.powo.portal.json.MainSearchBuilder;
import org.powo.portal.json.SearchResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonBuilderTest {

	private static Logger logger = LoggerFactory.getLogger(JsonBuilderTest.class);
	
	@Test
	public void resultBuilderTest(){
		SearchResultBuilder resultBuilder = new SearchResultBuilder();
		resultBuilder.url("http://fakeurl.org/10");
		resultBuilder.author("Blaaaarg");
		resultBuilder.rank("Family");
		resultBuilder.name("Blaaaargacea");
		
	}
	
	@Test
	public void resultBuilderWithImagesTest(){
		SearchResultBuilder resultBuilder = new SearchResultBuilder();
		resultBuilder.url("http://fakeurl.org/100");
		resultBuilder.author("Blaaaarg");
		resultBuilder.rank("Species");
		resultBuilder.name("Blaaaarga blaaarg");
		resultBuilder.addImage("http://fakeurl.org/image/1000_thumb.jpg", "http://fakeurl.org/image/1000_fullsize.jpg", "bear eating plant");
		
	}
	
	@Test
	public void mainBuilderTest(){
		SearchResultBuilder resultBuilder = new SearchResultBuilder();
		resultBuilder.url("http://fakeurl.org/100");
		resultBuilder.author("Blaaaarg");
		resultBuilder.rank("Species");
		resultBuilder.name("Blaaaarga blaaarg");
		resultBuilder.addImage("http://fakeurl.org/image/1000_thumb.jpg", "http://fakeurl.org/image/1000_fullsize.jpg", "bear eating plant");
		MainSearchBuilder mainJsonBuilder = new MainSearchBuilder();
		mainJsonBuilder.addResult(resultBuilder);
		mainJsonBuilder.sort("relevance_asc");
		mainJsonBuilder.page(0);
		mainJsonBuilder.perPage(10);
		mainJsonBuilder.totalResults(1000);
		
	}
	
}
