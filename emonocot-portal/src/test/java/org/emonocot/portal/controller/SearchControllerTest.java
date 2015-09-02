/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.portal.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.emonocot.api.SearchableObjectService;
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

	Model model;

	Page<SearchableObject> page;

	String[] facetNames;

	@Before
	public void setUp() throws Exception {
		searchController = new SearchController();
		facets = new ArrayList<FacetRequest>();
		model = new BindingAwareModelMap();
		searchableObjectService = EasyMock.createMock(SearchableObjectService.class);
		searchController.setSearchableObjectService(searchableObjectService);
		page = new DefaultPageImpl<SearchableObject>(0, 0, 20, new ArrayList<SearchableObject>(),null);
		facetNames = new String[] {"base.class_s", "taxon.family_ss", "taxon.distribution_TDWG_0_ss", "taxon.measurement_or_fact_threatStatus_txt", "taxon.measurement_or_fact_Lifeform_txt", "taxon.measurement_or_fact_Habitat_txt", "taxon.taxon_rank_s", "taxon.taxonomic_status_s", "searchable.sources_ss" };
	}

	/**
	 * When the class is image and the view is null, then the limit should be 24 and the view should be null
	 */
	@Test
	public void testSearchForImages() throws Exception {
		EasyMock.expect(searchableObjectService.search(EasyMock.eq(""), (String)EasyMock.isNull(), EasyMock.eq(24), EasyMock.eq(0), EasyMock.aryEq(facetNames),  (Map)EasyMock.isA(Map.class), EasyMock.isA(Map.class), (String)EasyMock.isNull(), EasyMock.eq("taxon-with-image"))).andReturn(page);

		EasyMock.replay(searchableObjectService);
		FacetRequest classFacet = new FacetRequest();
		classFacet.setFacet("base.class_s");
		classFacet.setSelected("org.emonocot.model.Image");
		facets.add(classFacet);
		String view = searchController.search("", 24, 0, facets, null, null, model);

		EasyMock.verify(searchableObjectService);
		assertEquals("View should equal 'search'","search",view);
		assertNull("The view attribute should be 'null'", page.getParams().get("view"));
	}

	/**
	 * When the class is all and the view is null, then the limit should be 24 and the view should be null
	 */
	@Test
	public void testSearchForAll() throws Exception {
		EasyMock.expect(searchableObjectService.search(EasyMock.eq(""), (String)EasyMock.isNull(), EasyMock.eq(24), EasyMock.eq(0), EasyMock.aryEq(facetNames),  (Map)EasyMock.isA(Map.class), (Map)EasyMock.isNull(), (String)EasyMock.isNull(), EasyMock.eq("taxon-with-image"))).andReturn(page);
		EasyMock.replay(searchableObjectService);

		String view = searchController.search("", 24, 0, facets, null, null, model);

		EasyMock.verify(searchableObjectService);
		assertEquals("View should equal 'search'","search",view);
		assertNull("The view attribute should be 'null'", page.getParams().get("view"));
	}

	/**
	 * When the class is all and the view is list, then the limit should be 24 and the view should be list
	 */
	@Test
	public void testSearchForAllListView() throws Exception {
		EasyMock.expect(searchableObjectService.search(EasyMock.eq(""), (String)EasyMock.isNull(), EasyMock.eq(24), EasyMock.eq(0), EasyMock.aryEq(facetNames),  (Map)EasyMock.isA(Map.class), (Map)EasyMock.isNull(), (String)EasyMock.isNull(), EasyMock.eq("taxon-with-image"))).andReturn(page);

		EasyMock.replay(searchableObjectService);

		String view = searchController.search("", 24, 0, facets, null, "list", model);

		EasyMock.verify(searchableObjectService);
		assertEquals("View should equal 'search'","search",view);
		assertEquals("The view attribute should be 'list'", page.getParams().get("view"),"list");
	}

	/**
	 * When the class is all and the view is grid, then the limit should be 24 and the view should be null
	 */
	@Test
	public void testSearchForAllGridView() throws Exception {
		EasyMock.expect(searchableObjectService.search(EasyMock.eq(""), (String)EasyMock.isNull(), EasyMock.eq(24), EasyMock.eq(0), EasyMock.aryEq(facetNames),  (Map)EasyMock.isA(Map.class), (Map)EasyMock.isNull(), (String)EasyMock.isNull(), EasyMock.eq("taxon-with-image"))).andReturn(page);

		EasyMock.replay(searchableObjectService);

		String view = searchController.search("", 24, 0, facets, null, "grid", model);

		EasyMock.verify(searchableObjectService);
		assertEquals("View should equal 'search'","search",view);
		assertEquals("The view attribute should be 'null'", page.getParams().get("view"), null);
	}

	/**
	 * BUG #333 eMonocot map search not displaying results 11-20
	 */
	@Test
	public void testPagination() throws Exception {
		EasyMock.expect(searchableObjectService.search(EasyMock.eq(""), (String)EasyMock.isNull(), EasyMock.eq(24), EasyMock.eq(1), EasyMock.aryEq(facetNames),  (Map)EasyMock.isA(Map.class), (Map)EasyMock.isNull(), (String)EasyMock.isNull(), EasyMock.eq("taxon-with-image"))).andReturn(page);

		EasyMock.replay(searchableObjectService);

		String view = searchController.search("", 24, 1, facets, null, "", model);

		EasyMock.verify(searchableObjectService);
		assertEquals("View should equal 'search'","search",view);
		assertEquals("The view attribute should be 'null'", page.getParams().get("view"), null);
	}

}
