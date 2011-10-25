package org.emonocot.persistence.olap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.common.AnnotationType;
import org.emonocot.persistence.AbstractPersistenceTest;
import org.emonocot.persistence.dao.JobDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.olap4j.CellSet;
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
    private JobDao jobDao;

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

        createAnnotation("Taxon", 1L, 1L, AnnotationType.Create);
        createAnnotation("Taxon", 1L, 2L, AnnotationType.Create);
        createAnnotation("Taxon", 1L, 3L, AnnotationType.Create);
        createAnnotation("Taxon", 1L, 4L, AnnotationType.Create);
        createAnnotation("Taxon", 2L, 1L, AnnotationType.Update);
    }

/**
    *
    */
   @Test
   public final void testJobDao() {
       assertNotNull(jobDao);
       List<JobExecution> jobExecutions = jobDao.getJobExecutions("WCS", 5);
       assertFalse(jobExecutions.isEmpty());
       assertEquals("Job identifier should match the expected value",
                jobExecutions.get(0).getId(), new Long(1));
       CellSet cellSet = jobDao.countObjects(1L);       
       cellSet = jobDao.countErrors(1L, "Taxon");
   }

}
