package org.emonocot.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.description.Feature;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.pager.Page;
import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.AnnotationDao;
import org.hibernate.search.query.facet.Facet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

/**
 *
 * @author ben
 *
 */
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AnnotationDaoTest extends AbstractPersistenceTest {

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
        //super.doTearDown();
    }

    /**
    *
    */
   @Override
    public final void setUpTestData() {
        Source wcs = createSource("WCS", "http://apps.kew.org/wcsTaxonExtractor");
       
        Taxon taxon1 = createTaxon("Aus", "1", null, null, null, null, null,
                null, null, null, wcs, new GeographicalRegion[] {}, null);
        createTextContent(taxon1, Feature.habitat, "Lorem ipsum", null);
        Taxon taxon2 = createTaxon("Aus bus", "2", taxon1, null, null, null,
                null, null, null, null, wcs,
                new GeographicalRegion[] {Continent.AUSTRALASIA,
                        Region.BRAZIL, Region.CARIBBEAN }, null);
        Taxon taxon3 = createTaxon("Aus ceus", "3", taxon1, null, null, null,
                null, null, null, null, wcs,
                new GeographicalRegion[] {Region.NEW_ZEALAND }, null);
        Taxon taxon4 = createTaxon("Aus deus", "4", null, taxon2, null, null,
                null, null, null, null, wcs, new GeographicalRegion[] {}, null);

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
   public final void testGetJobExecutions() {
       assertNotNull(annotationDao);
       FacetName[] facets = new FacetName[] {
    		   FacetName.AUTHORITY,
    		   FacetName.JOB_INSTANCE,
    		   FacetName.ERROR_CODE,
    		   FacetName.RECORD_TYPE
       };
       Map<FacetName,String> selectedFacets = new HashMap<FacetName, String>();
       selectedFacets.put(FacetName.AUTHORITY, "WCS");
       selectedFacets.put(FacetName.JOB_INSTANCE, "1");
       Page<Annotation> results = annotationDao.search(null, null, null, null, facets, selectedFacets, null, null);
       for(String facetName : results.getFacetNames()) {
    	   System.out.println(facetName);
    	   for(Facet facet : results.getFacets().get(facetName)) {
    		   System.out.println("\t" + facet.getValue() + " " + facet.getCount());
    	   }
       }
       assertFalse(results.getRecords().isEmpty());
       
       selectedFacets.clear();
       selectedFacets.put(FacetName.JOB_INSTANCE, "1");
       results = annotationDao.search(null, null, null, null, facets, selectedFacets, null, null);
       for(String facetName : results.getFacetNames()) {
    	   System.out.println(facetName);
    	   for(Facet facet : results.getFacets().get(facetName)) {
    		   System.out.println("\t" + facet.getValue() + " " + facet.getCount());
    	   }
       }
       selectedFacets.clear();
       selectedFacets.put(FacetName.JOB_INSTANCE, "1");
       selectedFacets.put(FacetName.RECORD_TYPE, "Taxon");
       selectedFacets.put(FacetName.ERROR_CODE, "Create");
       results = annotationDao.search(null, null, null, null, facets, selectedFacets, null, null);
       for(String facetName : results.getFacetNames()) {
    	   System.out.println(facetName);
    	   for(Facet facet : results.getFacets().get(facetName)) {
    		   System.out.println("\t" + facet.getValue() + " " + facet.getCount());
    	   }
       }

   }

}
