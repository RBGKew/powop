package org.emonocot.job.taxonmatch;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
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
import org.springframework.core.io.ClassPathResource;
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
    "/META-INF/spring/batch/jobs/taxonMatch.xml",
    "/META-INF/spring/applicationContext-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class TaxonMatchIntegrationTest {

    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(
            TaxonMatchIntegrationTest.class);

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
    public final void testMatchTaxa() throws IOException,
            NoSuchJobException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {
        ClassPathResource input = new ClassPathResource(
                "/org/emonocot/job/taxonmatch/input.csv");
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
        parameters.put("input.file", new JobParameter(input.getFile()
                .getAbsolutePath()));
        parameters.put("output.file",
                new JobParameter(File.createTempFile("output", "csv").getAbsolutePath()));

        JobParameters jobParameters = new JobParameters(parameters);

        Job taxonMatchingJob = jobLocator.getJob("TaxonMatching");
        assertNotNull("TaxonMatching must not be null", taxonMatchingJob);
        JobExecution jobExecution = jobLauncher.run(taxonMatchingJob,
                jobParameters);

        FileReader file = new FileReader(jobParameters.getParameters().get("output.file").getValue().toString());
        BufferedReader reader = new BufferedReader(file);
        assertNotNull("There should be an output file", reader);
        String ln;
        while ((ln = reader.readLine()) != null) {
            System.out.println(ln);
        }
    }
}
