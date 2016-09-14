package org.emonocot.portal.controller;

import org.emonocot.portal.json.MainSearchBuilder;
import org.emonocot.portal.json.SearchResultBuilder;
import org.junit.Test;
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
		resultBuilder.addImage("http://fakeurl.org/image/1000", "bear eating plant");
		
	}
	
	@Test
	public void mainBuilderTest(){
		SearchResultBuilder resultBuilder = new SearchResultBuilder();
		resultBuilder.url("http://fakeurl.org/100");
		resultBuilder.author("Blaaaarg");
		resultBuilder.rank("Species");
		resultBuilder.name("Blaaaarga blaaarg");
		resultBuilder.addImage("http://fakeurl.org/image/1000", "bear eating plant");
		MainSearchBuilder mainJsonBuilder = new MainSearchBuilder();
		mainJsonBuilder.addResult(resultBuilder);
		mainJsonBuilder.sort("relevance_asc");
		mainJsonBuilder.addFacet("base_searchable", 1000);
		mainJsonBuilder.addFacet("has_images", 100);
		mainJsonBuilder.addFacet("accepted", 10);
		mainJsonBuilder.page(0);
		mainJsonBuilder.perPage(10);
		mainJsonBuilder.totalResults(1000);
		
	}
	
}
