package org.emonocot.persistence.olap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.description.Feature;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.AbstractPersistenceTest;
import org.emonocot.persistence.dao.JobExecutionDao;
import org.emonocot.persistence.dao.OlapResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.olap4j.layout.CellSetFormatter;
import org.olap4j.layout.RectangularCellSetFormatter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class JobDaoTest extends AbstractPersistenceTest {

    /**
     *
     */
    @Autowired
    private JobExecutionDao jobDao;

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
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
        parameters.put("authority.name", new JobParameter("WCS"));
        JobInstance jobInstance = createJobInstance(1L, parameters, "testJob");
        super.createJobExecution(jobInstance);
        Taxon taxon1 = createTaxon("Aus", "1", null, null, null, null, null,
                null, null, null, null, new GeographicalRegion[] {});
        createTextContent(taxon1, Feature.habitat, "Lorem ipsum");
        Taxon taxon2 = createTaxon("Aus bus", "2", taxon1, null, null, null,
                null, null, null, null, null,
                new GeographicalRegion[] {Continent.AUSTRALASIA,
                        Region.BRAZIL, Region.CARIBBEAN });
        Taxon taxon3 = createTaxon("Aus ceus", "3", taxon1, null, null, null,
                null, null, null, null, null,
                new GeographicalRegion[] {Region.NEW_ZEALAND });
        Taxon taxon4 = createTaxon("Aus deus", "4", null, taxon2, null, null,
                null, null, null, null, null, new GeographicalRegion[] {});

        Annotation annotation1 = createAnnotation(1L, taxon1,
                AnnotationType.Info, RecordType.Taxon, AnnotationCode.Create);
        taxon1.getAnnotations().add(annotation1);
        Annotation annotation2 = createAnnotation(1L, taxon2,
                AnnotationType.Info, RecordType.Taxon, AnnotationCode.Create);
        taxon2.getAnnotations().add(annotation2);
        Annotation annotation3 = createAnnotation(1L, taxon3,
                AnnotationType.Info, RecordType.Taxon, AnnotationCode.Create);
        taxon3.getAnnotations().add(annotation3);
        Annotation annotation4 = createAnnotation(1L, taxon4,
                AnnotationType.Info, RecordType.Taxon, AnnotationCode.Create);
        taxon4.getAnnotations().add(annotation4);
        Annotation annotation5 = createAnnotation(2L, taxon1,
                AnnotationType.Info, RecordType.Taxon, AnnotationCode.Update);
        taxon1.getAnnotations().add(annotation5);
    }

/**
    *
    */
   @Test
   public final void testJobDao() {
       assertNotNull(jobDao);
        List<JobExecution> jobExecutions = jobDao.getJobExecutions("WCS", 5,
                null);
       assertFalse(jobExecutions.isEmpty());
       assertEquals("Job identifier should match the expected value",
                jobExecutions.get(0).getId(), new Long(1));
       CellSetFormatter formatter = new RectangularCellSetFormatter(false);

       List<OlapResult> results = jobDao.countObjects(1L);
       format(results, System.out);
       results = jobDao.countErrors(1L, "Taxon");
       format(results, System.out);
   }

   /**
    *
    * @param results Set the results
    * @param out Set the print stream
    */
    private void format(final List<OlapResult> results, final PrintStream out) {
        for (OlapResult result : results) {
            out.println("| " + result.getLabel() + " | "
                    + result.getValue() + " |");
        }
    }

}
