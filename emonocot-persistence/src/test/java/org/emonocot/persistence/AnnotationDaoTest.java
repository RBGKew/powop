package org.emonocot.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.emonocot.model.Annotation;
import org.emonocot.model.Source;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.RecordType;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.AnnotationDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
        super.doTearDown();
    }

    /**
    *
    */
   @Override
    public final void setUpTestData() {
        Source wcs = createSource("WCS", "http://apps.kew.org/wcsTaxonExtractor");
       
        Taxon taxon1 = createTaxon("Aus", "1", null, null, null, null, null,
                null, null, null, wcs, new GeographicalRegion[] {}, null);
        createDescription(taxon1, DescriptionType.habitat, "Lorem ipsum", null);
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
       String[] facets = new String[] {
    		   "base.authority_s",
    		   "annotation.job_id_l",
    		   "annotation.type_s",
    		   "annotation.record_type_s"
       };
       Map<String,String> selectedFacets = new HashMap<String, String>();
       selectedFacets.put("base.authority_s", "WCS");
       selectedFacets.put("annotation.job_id_l", "1");
       Page<Annotation> results = annotationDao.search(null, null, null, null, facets, selectedFacets, null, null);
       for(String facetName : results.getFacetNames()) {
    	   System.out.println(facetName);
    	   FacetField facet = results.getFacetField(facetName);
    	   for(Count count : facet.getValues()) {
    		   System.out.println("\t" + count.getName() + " " + count.getCount());
    	   }
       }
       assertFalse(results.getRecords().isEmpty());
       
       selectedFacets.clear();
       selectedFacets.put("annotation.job_id_l", "1");
       results = annotationDao.search(null, null, null, null, facets, selectedFacets, null, null);
       for(String facetName : results.getFacetNames()) {
    	   System.out.println(facetName);
    	   FacetField facet = results.getFacetField(facetName);
    	   for(Count count : facet.getValues()) {
    		   System.out.println("\t" + count.getName() + " " + count.getCount());
    	   }
       }
       selectedFacets.clear();
       selectedFacets.put("annotation.job_id_l", "1");
       selectedFacets.put("annotation.record_type_s", "Taxon");
       selectedFacets.put("annotation.type_s", "Create");
       results = annotationDao.search(null, null, null, null, facets, selectedFacets, null, null);
       for(String facetName : results.getFacetNames()) {
    	   System.out.println(facetName);
    	   FacetField facet = results.getFacetField(facetName);
    	   for(Count count : facet.getValues()) {
    		   System.out.println("\t" + count.getName() + " " + count.getCount());
    	   }
       }

   }

}
