package org.emonocot.job.key;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.Annotation;
import org.emonocot.model.Taxon;
import org.emonocot.persistence.hibernate.SolrIndexingListener;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "/META-INF/spring/batch/jobs/identificationKeyHarvesting.xml",
    "/META-INF/spring/applicationContext-integration.xml",
    "/META-INF/spring/applicationContext-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class IdentificationKeyJobIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(
            IdentificationKeyJobIntegrationTest.class);

    @Autowired
    private JobLocator jobLocator;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private SolrIndexingListener solrIndexingListener;

    /**
     * 1288569600 in unix time.
     */
    private static final BaseDateTime PAST_DATETIME
    = new DateTime(2010, 11, 1, 9, 0, 0, 0);

    /**
     *
     */
    @Before
	public final void setUp() {
		String fullSizeImagesDirectoryName = "./target/images/fullsize";
		File fullSizeImagesDirectory = new File(fullSizeImagesDirectoryName);
		fullSizeImagesDirectory.mkdirs();
		fullSizeImagesDirectory.deleteOnExit();
		String thumbnailImagesDirectoryName = "./target/images/thumbnails";
		File thumbnailImagesDirectory = new File(thumbnailImagesDirectoryName);
		thumbnailImagesDirectory.mkdirs();
		thumbnailImagesDirectory.deleteOnExit();
	}


    /**
     *
     * @throws IOException
     *             if a temporary file cannot be created.
     * @throws NoSuchJobException
     *             if SpeciesPageHarvestingJob cannot be located
     * @throws JobParametersInvalidException
     *             if the job parameters are invalid
     * @throws JobInstanceAlreadyCompleteException
     *             if the job has already completed
     * @throws JobRestartException
     *             if the job cannot be restarted
     * @throws JobExecutionAlreadyRunningException
     *             if the job is already running
     */
    @Test
    public final void testNotModifiedResponse() throws IOException,
            NoSuchJobException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {
        Session session = sessionFactory.openSession();        
        Transaction tx = session.beginTransaction();

        List<Taxon> taxa = session.createQuery("from Taxon as taxon").list();
        solrIndexingListener.indexObjects(taxa);
        tx.commit();

        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
        parameters.put("authority.name", new JobParameter("test"));
        parameters.put("root.taxon.identifier", new JobParameter("urn:kew.org:wcs:taxon:16026"));
        parameters.put("authority.uri", new JobParameter("http://build.e-monocot.org/test/testKey.xml"));
        parameters.put("authority.last.harvested", new JobParameter(Long.toString((IdentificationKeyJobIntegrationTest.PAST_DATETIME.getMillis()))));
        JobParameters jobParameters = new JobParameters(parameters);

        Job identificationKeyHarvestingJob = jobLocator.getJob("IdentificationKeyHarvesting");
        assertNotNull("IdentificationKeyHarvesting must not be null", identificationKeyHarvestingJob);
        JobExecution jobExecution = jobLauncher.run(identificationKeyHarvestingJob, jobParameters);
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            logger.info(stepExecution.getStepName() + " "
                    + stepExecution.getReadCount() + " "
                    + stepExecution.getFilterCount() + " "
                    + stepExecution.getWriteCount());
        }
        
        
        List<Annotation> annotations = session.createQuery("from Annotation a").list();
        for(Annotation a : annotations) {
        	logger.info(a.getJobId() + " " + a.getRecordType() + " " + a.getType() + " " + a.getCode() + " " + a.getText());
        }
    }
}
