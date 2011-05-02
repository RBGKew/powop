package org.emonocot.job.checklist;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.emonocot.job.scratchpads.HarvestingJobIntegrationTest;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "/META-INF/spring/batch/jobs/oaiPmhTaxonHarvesting.xml",
        "/applicationContext-test.xml" })
public class ChecklistHarvestingJobIntegrationTest {

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
     *
     */
    private static final BaseDateTime PAST_DATETIME
    = new DateTime(2010, 11, 1, 9, 0, 0, 0);

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
                "http://scratchpad.cate-araceae.org"));
        parameters.put("authority.uri", new JobParameter(
                "http://129.67.24.160/test/test.xml"));
        parameters
                .put("authority.last.harvested",
                     new JobParameter(Long.toString((
                     ChecklistHarvestingJobIntegrationTest.PAST_DATETIME
                                        .getMillis()))));
        parameters.put("temporary.file.name", new JobParameter(File
                .createTempFile("test", ".xml").getAbsolutePath()));
        JobParameters jobParameters = new JobParameters(parameters);

        Job oaiPmhTaxonHarvestingJob = jobLocator
                .getJob("OaiPmhTaxonHarvesting");
        assertNotNull("OaiPmhTaxonHarvestingJob must not be null",
                oaiPmhTaxonHarvestingJob);
        jobLauncher.run(oaiPmhTaxonHarvestingJob, jobParameters);
    }
}
