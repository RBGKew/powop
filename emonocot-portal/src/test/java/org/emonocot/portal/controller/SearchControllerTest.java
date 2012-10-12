package org.emonocot.portal.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.emonocot.api.FacetName;
import org.emonocot.api.ImageService;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.Sorting;
import org.emonocot.model.Image;
import org.emonocot.model.SearchableObject;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

public class SearchControllerTest {

	SearchController searchController;
	
	List<FacetRequest> facets;
	
	SearchableObjectService searchableObjectService;
	
	ImageService imageService;
	
	Model model;
	
	Page<Image> page;
	
	Page<SearchableObject> searchablePage;
	
	FacetName[] facetNames;
	
	@Before
	public void setUp() throws Exception {
		searchController = new SearchController();
		facets = new ArrayList<FacetRequest>();
		model = new BindingAwareModelMap();
		searchableObjectService = EasyMock.createMock(SearchableObjectService.class);
		imageService = EasyMock.createMock(ImageService.class);
		searchController.setSearchableObjectService(searchableObjectService);
		searchController.setImageService(imageService);
		page = new DefaultPageImpl<Image>(0, 0, 20, new ArrayList<Image>());
		searchablePage = new DefaultPageImpl<SearchableObject>(0, 0, 10, new ArrayList<SearchableObject>());
		facetNames = new FacetName[] {FacetName.CLASS, FacetName.FAMILY, FacetName.CONTINENT, FacetName.AUTHORITY};
	}

	/**
	 * When the class is image and the view is null, then the limit should be 20 and the view should be null
	 */
	@Test
	public void testSearchForImages() {
		EasyMock.expect(imageService.search(EasyMock.eq(""), (String)EasyMock.isNull(), EasyMock.eq(24), EasyMock.eq(0), EasyMock.aryEq(facetNames),  EasyMock.isA(Map.class), (Sorting)EasyMock.isNull(), EasyMock.eq("image-taxon"))).andReturn(page);
		
		EasyMock.replay(searchableObjectService,imageService);
		FacetRequest classFacet = new FacetRequest();
		classFacet.setFacet(FacetName.CLASS);
		classFacet.setSelected("org.emonocot.model.Image");
		facets.add(classFacet);
		String view = searchController.search("", 10, 0, facets, null, null, model);
		
		EasyMock.verify(searchableObjectService,imageService);
		assertEquals("View should equal 'search'","search",view);
		assertNull("The view attribute should be 'null'", page.getParams().get("view"));
	}
	
	/**
	 * When the class is all and the view is null, then the limit should be 10 and the view should be null
	 */
	@Test
	public void testSearchForAll() {
		EasyMock.expect(searchableObjectService.search(EasyMock.eq(""), (String)EasyMock.isNull(), EasyMock.eq(10), EasyMock.eq(0), EasyMock.aryEq(facetNames),  (Map)EasyMock.isNull(), (Sorting)EasyMock.isNull(), EasyMock.eq("taxon-with-image"))).andReturn(searchablePage);
		
		EasyMock.replay(searchableObjectService,imageService);
		
		String view = searchController.search("", 10, 0, facets, null, null, model);
		
		EasyMock.verify(searchableObjectService,imageService);
		assertEquals("View should equal 'search'","search",view);
		assertNull("The view attribute should be 'null'", searchablePage.getParams().get("view"));
	}
	
	/**
	 * When the class is all and the view is list, then the limit should be 10 and the view should be list
	 */
	@Test
	public void testSearchForAllListView() {
		EasyMock.expect(searchableObjectService.search(EasyMock.eq(""), (String)EasyMock.isNull(), EasyMock.eq(10), EasyMock.eq(0), EasyMock.aryEq(facetNames),  (Map)EasyMock.isNull(), (Sorting)EasyMock.isNull(), EasyMock.eq("taxon-with-image"))).andReturn(searchablePage);
		
		EasyMock.replay(searchableObjectService,imageService);
		
		String view = searchController.search("", 10, 0, facets, null, "list", model);
		
		EasyMock.verify(searchableObjectService,imageService);
		assertEquals("View should equal 'search'","search",view);
		assertEquals("The view attribute should be 'list'", searchablePage.getParams().get("view"),"list");
	}
	
	/**
	 * When the class is all and the view is grid, then the limit should be 20 and the view should be grid
	 */
	@Test
	public void testSearchForAllGridView() {
		EasyMock.expect(searchableObjectService.search(EasyMock.eq(""), (String)EasyMock.isNull(), EasyMock.eq(24), EasyMock.eq(0), EasyMock.aryEq(facetNames),  (Map)EasyMock.isNull(), (Sorting)EasyMock.isNull(), EasyMock.eq("taxon-with-image"))).andReturn(searchablePage);
		
		EasyMock.replay(searchableObjectService,imageService);
		
		String view = searchController.search("", 10, 0, facets, null, "grid", model);
		
		EasyMock.verify(searchableObjectService,imageService);
		assertEquals("View should equal 'search'","search",view);
		assertEquals("The view attribute should be 'grid'", searchablePage.getParams().get("view"),"grid");
	}
	
	/**
	 * BUG #333 eMonocot map search not displaying results 11-20
	 */
	@Test
	public void testPagination() {
        EasyMock.expect(searchableObjectService.search(EasyMock.eq(""), (String)EasyMock.isNull(), EasyMock.eq(10), EasyMock.eq(1), EasyMock.aryEq(facetNames),  (Map)EasyMock.isNull(), (Sorting)EasyMock.isNull(), EasyMock.eq("taxon-with-image"))).andReturn(searchablePage);
		
		EasyMock.replay(searchableObjectService,imageService);
		
		String view = searchController.search("", 10, 1, facets, null, "", model);
		
		EasyMock.verify(searchableObjectService,imageService);
		assertEquals("View should equal 'search'","search",view);
		assertEquals("The view attribute should be ''", searchablePage.getParams().get("view"),"");
	}

}
