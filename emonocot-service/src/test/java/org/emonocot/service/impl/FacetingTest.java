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
package org.emonocot.service.impl;

import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.response.FacetField;
import org.emonocot.api.AnnotationService;
import org.emonocot.api.ImageService;
import org.emonocot.api.OrganisationService;
import org.emonocot.api.PlaceService;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.TaxonService;
import org.emonocot.model.Annotation;
import org.emonocot.model.Image;
import org.emonocot.model.Place;
import org.emonocot.model.SearchableObject;
import org.emonocot.model.Taxon;
import org.emonocot.model.auth.User;
import org.emonocot.model.constants.Location;
import org.emonocot.model.registry.Organisation;
import org.emonocot.pager.Page;
import org.emonocot.test.DataManagementSupport;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/spring/applicationContext*.xml" })
public class FacetingTest extends DataManagementSupport {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TaxonService taxonService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private AnnotationService annotationService;

	@Autowired
	private OrganisationService sourceService;

	@Autowired
	private PlaceService placeService;

	@Autowired
	private SearchableObjectService searchableObjectService;

	private User user;

	private UsernamePasswordAuthenticationToken token;

	/**
	 * @throws java.lang.Exception if there is a problem
	 */
	@Before
	public final void setUp() throws Exception {
		setUpTestData();

		for (Object obj : getSetUp()) {
			if (obj.getClass().equals(Taxon.class)) {
				taxonService.saveOrUpdate((Taxon) obj);
			} else if (obj.getClass().equals(Image.class)) {
				imageService.saveOrUpdate((Image) obj);
			} else if (obj.getClass().equals(Annotation.class)) {
				annotationService.saveOrUpdate((Annotation) obj);
			} else if (obj.getClass().equals(Organisation.class)) {
				sourceService.saveOrUpdate((Organisation) obj);
			} else if (obj.getClass().equals(Place.class)) {
				placeService.saveOrUpdate((Place) obj);
			}
		}
		token = new UsernamePasswordAuthenticationToken("admin@e-monocot.org", "sPePhAz6");
		Authentication authentication = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		SecurityContextHolder.clearContext();
	}

	/**
	 * @throws java.lang.Exception if there is a problem
	 */
	@After
	public final void tearDown() throws Exception {
		setSetUp(new ArrayList<Object>());
		token = new UsernamePasswordAuthenticationToken("admin@e-monocot.org",
				"sPePhAz6");
		Authentication authentication = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		while (!getTearDown().isEmpty()) {
			Object obj = getTearDown().pop();
			if (obj.getClass().equals(Taxon.class)) {
				taxonService.delete(((Taxon) obj).getIdentifier());
			} else if (obj.getClass().equals(Image.class)) {
				imageService.delete(((Image) obj).getIdentifier());
			} else if (obj.getClass().equals(Annotation.class)) {
				annotationService.delete(((Annotation) obj).getIdentifier());
			} else if (obj.getClass().equals(Organisation.class)) {
				sourceService.delete(((Organisation) obj).getIdentifier());
			} else if (obj.getClass().equals(Place.class)) {
				placeService.delete(((Place) obj).getIdentifier());
			}
		}
		SecurityContextHolder.clearContext();
	}

	/**
	 *
	 */
	@Override
	public final void setUpTestData() {
		Organisation source1 = createSource("test", "http://example.com", "Test Organisation", "test@example.com");
		Organisation source2 = createSource("source2", "http://source2.com", "Test Organisation 2", "test@example2.com");
		Taxon taxon1 = createTaxon("Aus", "1", null, null, "Ausaceae", null,
				null, "(1753)", Rank.GENUS, TaxonomicStatus.Accepted,
				source1, new Location[] {}, new Organisation[] {source1});
		Taxon taxon2 = createTaxon("Aus bus", "2", taxon1, null, "Ausaceae",
				null, null, "(1775)", Rank.SPECIES, TaxonomicStatus.Accepted,
				source1, new Location[] {Location.AUSTRALASIA,
				Location.BRAZIL, Location.CARIBBEAN }, new Organisation[] {source1,source2});
		Taxon taxon3 = createTaxon("Aus ceus", "3", taxon1, null, "Ausaceae",
				null, null, "(1805)", Rank.SPECIES, TaxonomicStatus.Accepted,
				source1, new Location[] {Location.NEW_ZEALAND }, new Organisation[] {source1,source2});
		Taxon taxon4 = createTaxon("Aus deus", "4", null, taxon2, "Ausaceae",
				null, null, "(1895)", Rank.SPECIES, TaxonomicStatus.Synonym,
				source1, new Location[] {}, new Organisation[] {source1});
		Taxon taxon5 = createTaxon("Aus eus", "5", null, taxon3, "Ausaceae",
				null, null, "(1935)", Rank.SPECIES, TaxonomicStatus.Synonym,
				source1, new Location[] {}, new Organisation[] {source1});
		Image img1 = createImage("Aus", "1", source2,taxon2, new Organisation[] {source2,source1});
		Image img2 = createImage("Aus bus", "2", source2,taxon2, new Organisation[] {source2,source1});

		Place place1 = createPlace("gb", "Great Britain");

	}

	/**
	 *
	 */
	@Test
	public final void testSearch() throws Exception {
		Page<SearchableObject> pager = searchableObjectService.search("Aus",
				null, null, null, null, null, null, null, null);
		assertEquals("there should be seven objects saved", (Integer) 7,
				pager.getSize());
	}

	/**
	 *
	 */
	@Test
	public final void testSearchWithFacets() throws Exception {
		Map<String, String> selectedFacets = new HashMap<String, String>();
		selectedFacets.put("base.class_s", "org.emonocot.model.Taxon");
		Page<SearchableObject> pager = searchableObjectService.search("Aus",
				null, null, null, new String[] {"base.class_s",
				"taxon.family_ss", "taxon.distribution_TDWG_0_ss",
		"searchable.sources_ss" },
		null, selectedFacets, null, null);
		assertThat("There should be two facets returned",
				pager.getFacetNames(), containsInAnyOrder("base.class_s",
						"taxon.family_ss", "taxon.distribution_TDWG_0_ss",
						"searchable.sources_ss"));

		FacetField classFacet = pager.getFacetField("base.class_s");
		String[] Strings = new String[classFacet.getValueCount()];
		for (int i = 0; i < Strings.length; i++) {
			Strings[i] = classFacet.getValues().get(i).getName();
		}

		assertThat("org.emonocot.model.Taxon should be a facet in CLASS",
				Strings, hasItemInArray("org.emonocot.model.Taxon"));
		assertEquals("There should be one value for the FAMILY facet", 1, pager
				.getFacetField("taxon.family_ss").getValues().size());
	}

	/**
	 *
	 */
	@Test
	public final void testSearchWithRegion() throws Exception {
		Map<String, String> selectedFacets = new HashMap<String, String>();
		selectedFacets.put("base.class_s", "org.emonocot.model.Taxon");
		selectedFacets.put("taxon.distribution_TDWG_1_ss", "AUSTRALASIA_NEW_ZEALAND");
		Page<SearchableObject> pager = searchableObjectService.search("Aus",
				null, null, null, new String[] {"base.class_s",
				"taxon.family_ss", "taxon.distribution_TDWG_0_ss",
				"taxon.distribution_TDWG_1_ss", "searchable.sources_ss" },
				null, selectedFacets, null, null);
		assertThat("There should be two facets returned",
				pager.getFacetNames(), containsInAnyOrder("base.class_s",
						"taxon.family_ss", "taxon.distribution_TDWG_0_ss",
						"taxon.distribution_TDWG_1_ss", "searchable.sources_ss"));

		FacetField classFacet = pager.getFacetField("base.class_s");
		String[] Strings = new String[classFacet.getValueCount()];
		for (int i = 0; i < Strings.length; i++) {
			Strings[i] = classFacet.getValues().get(i).getName();
		}

		assertThat("org.emonocot.model.Taxon should be a facet in CLASS",
				Strings, hasItemInArray("org.emonocot.model.Taxon"));
		assertEquals("There should be one value for the FAMILY facet", 1, pager
				.getFacetField("taxon.family_ss").getValueCount());
	}

	/**
	 * @throws Exception
	 *             if there is a problem tearing down and adding the test data
	 */
	@Test
	public final void testSearchWithFacetsInTaxonDao() throws Exception {
		Map<String, String> selectedFacets = new HashMap<String, String>();
		selectedFacets.put("base.class_s", "org.emonocot.model.Taxon");
		Page<SearchableObject> pager = searchableObjectService.search("Aus", null, null, null,
				new String[] { "base.class_s", "taxon.family_ss",
				"taxon.distribution_TDWG_0_ss", "searchable.sources_ss",
				"taxon.taxon_rank_s", "taxon.taxonomic_status_s" },
				null, selectedFacets, null, null);
		assertEquals("There should be five taxa returned", (Integer) 5,
				pager.getSize());
		assertThat("There should be six facets returned",
				pager.getFacetNames(), containsInAnyOrder("base.class_s", "taxon.family_ss",
						"taxon.distribution_TDWG_0_ss", "searchable.sources_ss",
						"taxon.taxon_rank_s", "taxon.taxonomic_status_s"));

		FacetField classFacet = pager.getFacetField("base.class_s");
		String[] Strings = new String[classFacet.getValueCount()];
		for (int i = 0; i < Strings.length; i++) {
			Strings[i] = classFacet.getValues().get(i).getName();
		}

		assertThat("org.emonocot.model.Taxon should be a facet in CLASS",
				Strings, hasItemInArray("org.emonocot.model.Taxon"));
		assertEquals("There should be one values for the FAMILY facet", 1, pager
				.getFacetField("taxon.family_ss").getValueCount());

		selectedFacets.put("taxon.taxon_rank_s", "SPECIES");
		pager = searchableObjectService.search("Aus", null, null, null,
				new String[] {"base.class_s", "taxon.family_ss",
				"taxon.distribution_TDWG_0_ss", "searchable.sources_ss",
				"taxon.taxon_rank_s", "taxon.taxonomic_status_s" },
				null, selectedFacets, null, null);
		assertEquals("There should be four taxa returned", (Integer) 4,
				pager.getSize());
	}

	/**
	 *
	 */
	@Test
	public final void testSearchWithSorting() throws Exception {
		Page<SearchableObject> results = searchableObjectService.search("Au*",
				null, null, null, null, null, null, null, null);

		String sort = "searchable.label_sort_asc";
		results = searchableObjectService.search("Au*", null, null, null, null,
				null, null, sort, null);
		String[] actual = new String[results.getSize()];
		for (int i = 0; i < results.getSize(); i++) {
			if (results.getRecords().get(i).getClassName().equals("Taxon")) {
				actual[i] = ((Taxon) results.getRecords().get(i)).getScientificName();
			} else {
				actual[i] = ((Image) results.getRecords().get(i)).getTitle();
			}
		}

		String[] expected = new String[] {"Aus", "Aus", "Aus bus", "Aus bus",
				"Aus ceus", "Aus deus", "Aus eus" };

		assertArrayEquals(expected, actual);
	}

	@Test
	public final void testFacetOnSource() throws Exception {
		Map<String, String> selectedFacets = new HashMap<String, String>();
		Page<?> results = searchableObjectService.search(null,
				null, null, null,
				new String[] {"base.class_s", "taxon.family_ss", "taxon.distribution_TDWG_0_ss","searchable.sources_ss" },
				null, null, null, null);
		selectedFacets.clear();
		selectedFacets.put("taxon.family_ss", "Ausaceae");
		results = searchableObjectService.search(null,
				null, null, null,
				new String[] {"base.class_s", "taxon.family_ss", "taxon.distribution_TDWG_0_ss","searchable.sources_ss" },
				null, selectedFacets, null, null);
		selectedFacets.clear();
		selectedFacets.put("taxon.family_ss", "Ausaceae");
		selectedFacets.put("base.class_s", "org.emonocot.model.taxon.Taxon");
		selectedFacets.put("searchable.sources_ss", "source2");


		results = searchableObjectService.search(null,
				null, null, null,
				new String[] {"base.class_s","taxon.family_ss", "taxon.distribution_TDWG_0_ss","searchable.sources_ss"},
				null, selectedFacets, null, null);
		selectedFacets.clear();
		selectedFacets.put("searchable.sources_ss", "source2");


		results = searchableObjectService.search(null,
				null, null, null,
				new String[] {"base.class_s","taxon.family_ss", "taxon.distribution_TDWG_0_ss","searchable.sources_ss"},
				null, selectedFacets, null, null);
	}

	/**
	 * BUG # 334  As a user of eMonocot I want to be able to perform a faceted search by place
	 */
	@Test
	public final void testFacetOnPlace() throws Exception {
		Map<String, String> selectedFacets = new HashMap<String, String>();
		selectedFacets.put("base.class_s", "org.emonocot.model.Place");
		Page<SearchableObject> places = searchableObjectService.search(null, null, 10, 0, new String[] {}, null, selectedFacets, null, null);
		assertEquals("There should be one place in the result list",(Integer)places.getSize(),(Integer)1);
	}
}
