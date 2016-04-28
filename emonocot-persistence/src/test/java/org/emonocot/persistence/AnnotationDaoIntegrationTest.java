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
package org.emonocot.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.emonocot.model.Annotation;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.Location;
import org.emonocot.model.constants.RecordType;
import org.emonocot.model.registry.Organisation;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.AnnotationDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author ben
 *
 */

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AnnotationDaoIntegrationTest extends AbstractPersistenceTest {

	private static Logger logger = LoggerFactory.getLogger(AnnotationDaoIntegrationTest.class);

	/**
	 *
	 */
	@Autowired
	private AnnotationDao annotationDao;

	/**
	 * @throws java.lang.Exception if there is a problem
	 */
	@Before
	public final void setUp() throws Exception {
		super.doSetUp();
	}

	/**
	 * @throws java.lang.Exception if there is a problem
	 */
	@After
	public final void tearDown() throws Exception {
		super.doTearDown();
	}

	/**
	 *
	 */
	@Override
	public final void setUpTestData() {
		Organisation wcs = createSource("WCS", "http://apps.kew.org/wcsTaxonExtractor", "World Checklist", "test@example.com");

		Taxon taxon1 = createTaxon("Aus", "1", null, null, null, null, null,
				null, null, null, wcs, new Location[] {}, null);
		createDescription(taxon1, DescriptionType.habitat, "Lorem ipsum", null);
		Taxon taxon2 = createTaxon("Aus bus", "2", taxon1, null, null, null,
				null, null, null, null, wcs,
				new Location[] {Location.AUSTRALASIA,
				Location.BRAZIL, Location.CARIBBEAN }, null);
		Taxon taxon3 = createTaxon("Aus ceus", "3", taxon1, null, null, null,
				null, null, null, null, wcs,
				new Location[] {Location.NEW_ZEALAND }, null);
		Taxon taxon4 = createTaxon("Aus deus", "4", null, taxon2, null, null,
				null, null, null, null, wcs, new Location[] {}, null);

		Annotation annotation1 = createAnnotation(1L, taxon1,
				AnnotationType.Info, RecordType.Taxon, AnnotationCode.Create, wcs);
		taxon1.getAnnotations().add(annotation1);
		Annotation annotation2 = createAnnotation(1L, taxon2,
				AnnotationType.Info, RecordType.Taxon, AnnotationCode.Create, wcs);
		taxon2.getAnnotations().add(annotation2);
		Annotation annotation3 = createAnnotation(1L, taxon3,
				AnnotationType.Info, RecordType.Taxon, AnnotationCode.Create, wcs);
		taxon3.getAnnotations().add(annotation3);
		Annotation annotation4 = createAnnotation(1L, taxon4,
				AnnotationType.Error, RecordType.Taxon, AnnotationCode.Create, wcs);
		taxon4.getAnnotations().add(annotation4);
		Annotation annotation5 = createAnnotation(2L, taxon1,
				AnnotationType.Error, RecordType.Taxon, AnnotationCode.Update, wcs);
		taxon1.getAnnotations().add(annotation5);
	}

	/**
	 *
	*/
	@Test
	public final void testGetJobExecutions() throws Exception {
		assertNotNull(annotationDao);
		String[] facets = new String[] {
				"base.authority_s",
				"annotation.job_id_l",
				"annotation.type_s",
				"annotation.record_type_s"
		};
		Map<String,String> selectedFacets = new HashMap<String, String>();
		selectedFacets.put("base.authority_s", "WCS");
		selectedFacets.put("annotation.job_id_l", "1");
		Page<Annotation> results = annotationDao.search(null, null, null, null, facets, null, selectedFacets, null, null);
		for(String facetName : results.getFacetNames()) {
			logger.debug(facetName);
			FacetField facet = results.getFacetField(facetName);
			for(Count count : facet.getValues()) {
				logger.debug("\t" + count.getName() + " " + count.getCount());
			}
		}
		assertFalse(results.getRecords().isEmpty());

		selectedFacets.clear();
		selectedFacets.put("annotation.job_id_l", "1");
		results = annotationDao.search(null, null, null, null, facets, null, selectedFacets, null, null);
		for(String facetName : results.getFacetNames()) {
			logger.debug(facetName);
			FacetField facet = results.getFacetField(facetName);
			for(Count count : facet.getValues()) {
				logger.debug("\t" + count.getName() + " " + count.getCount());
			}
		}
		selectedFacets.clear();
		selectedFacets.put("annotation.job_id_l", "1");
		selectedFacets.put("annotation.record_type_s", "Taxon");
		selectedFacets.put("annotation.type_s", "Create");
		results = annotationDao.search(null, null, null, null, facets, null, selectedFacets, null, null);
		for(String facetName : results.getFacetNames()) {
			logger.debug(facetName);
			FacetField facet = results.getFacetField(facetName);
			for(Count count : facet.getValues()) {
				logger.debug("\t" + count.getName() + " " + count.getCount());
			}
		}

	}

}
