package org.emonocot.job.dwc;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    "/META-INF/spring/batch/jobs/darwinCoreArchiveHarvesting.xml",
    "/META-INF/spring/applicationContext-integration.xml",
    "/META-INF/spring/applicationContext-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DarwinCoreJobIntegrationTest {

    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(
            DarwinCoreJobIntegrationTest.class);

    /**
     *
     */
    @Autowired
    private JobLocator jobLocator;

    /**
     *
     */
    @Autowired
    private JobLauncher jobLauncher;

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
        File imageDirectory = new File("./target/images");
        imageDirectory.mkdir();
        File thumbnailDirectory = new File("./target/thumbnails");
        thumbnailDirectory.mkdir();
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
        Map<String, JobParameter> parameters =
            new HashMap<String, JobParameter>();
        parameters.put("authority.name", new JobParameter(
                "test"));
        parameters.put("family", new JobParameter(
        "Arecaceae"));
        parameters.put("authority.uri", new JobParameter(
                "http://dioscoreaceae.e-monocot.org/dwca.zip"));
        parameters.put(
                "authority.last.harvested",
                new JobParameter(Long
                        .toString((DarwinCoreJobIntegrationTest.PAST_DATETIME
                                .getMillis()))));
        JobParameters jobParameters = new JobParameters(parameters);

        Job darwinCoreArchiveHarvestingJob = jobLocator
                .getJob("DarwinCoreArchiveHarvesting");
        assertNotNull("DarwinCoreArchiveHarvesting must not be null",
                darwinCoreArchiveHarvestingJob);
        JobExecution jobExecution = jobLauncher.run(darwinCoreArchiveHarvestingJob, jobParameters);
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            logger.info(stepExecution.getStepName() + " "
                    + stepExecution.getReadCount() + " "
                    + stepExecution.getFilterCount() + " "
                    + stepExecution.getWriteCount() + " " + stepExecution.getCommitCount());
        }
    }
}
