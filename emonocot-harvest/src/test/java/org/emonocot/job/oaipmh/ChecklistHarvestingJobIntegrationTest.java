package org.emonocot.job.oaipmh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.TaxonService;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ben
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "/META-INF/spring/batch/jobs/oaiPmhTaxonHarvesting.xml",
        "/META-INF/spring/applicationContext-integration.xml",
        "/META-INF/spring/applicationContext-test.xml" })
public class ChecklistHarvestingJobIntegrationTest {

    /**
     *
     */
    private Logger logger = LoggerFactory
            .getLogger(ChecklistHarvestingJobIntegrationTest.class);

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
    @Autowired
    private TaxonService taxonService;

    /**
     * 1288569600 in unix time.
     */
    static final BaseDateTime PAST_DATETIME = new DateTime(2010, 11, 1, 9, 0,
            0, 0);

    /**
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
    public final void testOaiPmhHarvest() throws IOException,
            NoSuchJobException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {

        if (taxonService.load("urn:lsid:grassbase.kew.org:taxa:387039") == null) {
            fail("The taxon we need to delete is not present");
        }

        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
        parameters.put("authority.name", new JobParameter("test"));
        parameters.put("authority.uri", new JobParameter(
                "http://build.e-monocot.org/test/oai.xml"));
        parameters
                .put("authority.last.harvested",
                        new JobParameter(
                                Long.toString((ChecklistHarvestingJobIntegrationTest.PAST_DATETIME
                                        .getMillis()))));
        parameters.put("request.interval", new JobParameter("10000"));
        JobParameters jobParameters = new JobParameters(parameters);

        Job oaiPmhTaxonHarvestingJob = jobLocator
                .getJob("OaiPmhTaxonHarvesting");
        assertNotNull("OaiPmhTaxonHarvestingJob must not be null",
                oaiPmhTaxonHarvestingJob);
        JobExecution jobExecution = jobLauncher.run(oaiPmhTaxonHarvestingJob,
                jobParameters);
        assertEquals("Job should complete normally",
                jobExecution.getExitStatus(), ExitStatus.COMPLETED);

        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            logger.info(stepExecution.getStepName() + " "
                    + stepExecution.getReadCount() + " "
                    + stepExecution.getFilterCount() + " "
                    + stepExecution.getWriteCount());
       }

        assertNull("We should have deleted this taxon",
                taxonService.find("urn:lsid:grassbase.kew.org:taxa:387039"));
        assertEquals("The specific epithet should have been updated",
                "johowii", taxonService.find("urn:kew.org:wcs:taxon:303017")
                        .getSpecificEpithet());
    }
}
