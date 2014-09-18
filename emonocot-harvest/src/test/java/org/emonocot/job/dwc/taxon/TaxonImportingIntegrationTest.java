package org.emonocot.job.dwc.taxon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.emonocot.api.ReferenceService;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
    "/META-INF/spring/batch/jobs/darwinCoreArchiveHarvesting.xml",
    "/META-INF/spring/applicationContext-integration.xml",
    "/META-INF/spring/applicationContext-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class TaxonImportingIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(TaxonImportingIntegrationTest.class);

    @Autowired
    private JobLocator jobLocator;

    @Autowired
	@Qualifier("readWriteJobLauncher")
    private JobLauncher jobLauncher;
    
    private Properties properties;
    
    @Autowired
    private ReferenceService referenceService;

    /**
     * 1288569600 in unix time.
     */
    private static final BaseDateTime PAST_DATETIME
    = new DateTime(2010, 11, 1, 9, 0, 0, 0);
    
    @Before
    public final void setUp() throws Exception {
        File imageDirectory = new File("./target/images/fullsize");
        imageDirectory.mkdirs();
        imageDirectory.deleteOnExit();
        File spoolDirectory = new File("./target/spool");
        spoolDirectory.mkdirs();
        spoolDirectory.deleteOnExit();
        File thumbnailDirectory = new File("./target/images/thumbnails");
        thumbnailDirectory.mkdirs();
        thumbnailDirectory.deleteOnExit();
        Resource propertiesFile = new ClassPathResource("META-INF/spring/application.properties");
        properties = new Properties();
        properties.load(propertiesFile.getInputStream());
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
    public final void testImportTaxa() throws IOException,
            NoSuchJobException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {
        Map<String, JobParameter> parameters =
            new HashMap<String, JobParameter>();
        parameters.put("authority.name", new JobParameter(
                "test"));
        parameters.put("family", new JobParameter(
        "Araceae"));
        parameters.put("taxon.processing.mode", new JobParameter("IMPORT_TAXA_BY_AUTHORITY"));
        String repository = properties.getProperty("test.resource.baseUrl",
                "http://build.e-monocot.org/git/?p=emonocot.git;a=blob_plain;f=emonocot-harvest/src/test/resources/org/emonocot/job/common/");
        parameters.put("authority.uri", new JobParameter(repository + "dwc.zip"));
        parameters.put("authority.last.harvested",
                new JobParameter(Long.toString((TaxonImportingIntegrationTest.PAST_DATETIME.getMillis()))));
        JobParameters jobParameters = new JobParameters(parameters);

        Job darwinCoreArchiveHarvestingJob = jobLocator.getJob("DarwinCoreArchiveHarvesting");
        assertNotNull("DarwinCoreArchiveHarvesting must not be null", darwinCoreArchiveHarvestingJob);
        JobExecution jobExecution = jobLauncher.run(darwinCoreArchiveHarvestingJob, jobParameters);
        assertEquals("The job should complete successfully",jobExecution.getExitStatus().getExitCode(),"COMPLETED");
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            logger.info(stepExecution.getStepName() + " "
                    + stepExecution.getReadCount() + " "
                    + stepExecution.getFilterCount() + " "
                    + stepExecution.getWriteCount());
        }
        
        //Test namePublishedIn is saved
        //assertNotNull("The namePublishedIn should have been saved.",
        //        referenceService.findByBibliographicCitation("Pageter, A (2014) Hedgerow Woe"));
    }
}
