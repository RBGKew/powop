package org.emonocot.job.sitemap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "/META-INF/spring/batch/jobs/sitemap.xml",
        "/META-INF/spring/applicationContext-integration.xml",
        "/META-INF/spring/applicationContext-test.xml" })
public class SitemapGenerationIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(SitemapGenerationIntegrationTest.class);

    @Autowired
    private JobLocator jobLocator;

    @Autowired
	@Qualifier("jobLauncher")
    private JobLauncher jobLauncher;

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
    public final void testRun() throws Exception {

        Job sitemapJob = jobLocator
                .getJob("SitemapGeneration");
        assertNotNull("SitemapGeneration Job must not be null",
                sitemapJob);
        Exception ex = null;
        JobExecution jobExecution = null;
        try{
        	jobExecution = jobLauncher.run(sitemapJob, new JobParameters());
        } catch (Exception e) {
        	ex = e;
			logger.info("Tolerating this error for now");
		}
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            logger.info(stepExecution.getStepName() + " "
                    + stepExecution.getReadCount() + " "
                    + stepExecution.getFilterCount() + " "
                    + stepExecution.getWriteCount());
        }
        if (ex != null){
        	throw ex;
        }
        
        assertEquals("Job should complete normally", ExitStatus.COMPLETED,
                jobExecution.getExitStatus());

    }
}
