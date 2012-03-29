package org.emonocot.job.checklist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.TaxonService;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
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
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test which verifies that we can delete taxa, especially the rather tricky
 * case where related taxa are deleted and appear in the same chunk, leading to
 * hibernate issues on cascade.
 *
 * BUG 179 Processing deleted taxa
 * http://build.e-monocot.org/bugzilla/show_bug.cgi?id=179
 *
 * @author ben
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "/META-INF/spring/batch/jobs/oaiPmhTaxonHarvesting.xml",
        "/META-INF/spring/applicationContext-integration.xml",
        "/META-INF/spring/applicationContext-test.xml" })
public class DeletingTaxaIntegrationTest {

    /**
     *
     */
    private Logger logger = LoggerFactory
            .getLogger(DeletingTaxaIntegrationTest.class);

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
    private JobLauncherTestUtils jobLauncherTestUtils;

    /**
     *
     */
    @Autowired
    private TaxonService taxonService;

    /**
     *
     */
    private File tempFile;

    /**
     * @throws IOException
     *             if there is a problem opening the temporary file
     *
     */
    @Before
    public final void setUp() throws IOException {
        ClassPathResource tempFileResource = new ClassPathResource(
                "org/emonocot/job/oai/DeletedTaxa.xml");
        tempFile = tempFileResource.getFile();
    }

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
    public final void testDeleteTaxa() throws IOException, NoSuchJobException,
            JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        assertNotNull("urn:kew.org:wcs:taxon:70053 should exist",
                taxonService.find("urn:kew.org:wcs:taxon:70053"));
        assertNotNull("urn:kew.org:wcs:taxon:70052 should exist",
                taxonService.find("urn:kew.org:wcs:taxon:70052"));
        assertNull("urn:kew.org:wcs:taxon:467051 should not exist",
                taxonService.find("urn:kew.org:wcs:taxon:467051"));
        assertEquals("urn:kew.org:wcs:taxon:71680 should have one child taxon",
                1, taxonService.find("urn:kew.org:wcs:taxon:71680")
                        .getChildren().size());

        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
        parameters.put("authority.name", new JobParameter("test"));
        parameters.put("authority.uri", new JobParameter(
                "http://build.e-monocot.org/test/oai.xml"));
        parameters.put(
                "authority.last.harvested",
                new JobParameter(Long
                        .toString((DeletingTaxaIntegrationTest.PAST_DATETIME
                                .getMillis()))));
        parameters.put("request.interval", new JobParameter("10000"));

        JobParameters jobParameters = new JobParameters(parameters);
        ExecutionContext jobExecutionContext = new ExecutionContext();
        jobExecutionContext.put("temporary.file.name",
                tempFile.getAbsolutePath());

        JobExecution jobExecution = jobLauncherTestUtils.launchStep(
                "processRecords", jobParameters, jobExecutionContext);

        assertEquals("Job should complete normally",
                jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        assertNull("urn:kew.org:wcs:taxon:70053 should be deleted",
                taxonService.find("urn:kew.org:wcs:taxon:70053"));
        assertNull("urn:kew.org:wcs:taxon:70052 should be deleted",
                taxonService.find("urn:kew.org:wcs:taxon:70052"));
        assertNull("urn:kew.org:wcs:taxon:467051 should be deleted",
                taxonService.find("urn:kew.org:wcs:taxon:467051"));
        assertNotNull("urn:kew.org:wcs:taxon:345361 should be saved",
                taxonService.find("urn:kew.org:wcs:taxon:345361"));
        assertNotNull("urn:kew.org:wcs:taxon:345362 should be saved",
                taxonService.find("urn:kew.org:wcs:taxon:345362"));
        assertNotNull("urn:kew.org:wcs:taxon:345506 should be saved",
                taxonService.find("urn:kew.org:wcs:taxon:345506"));
        assertNotNull("urn:kew.org:wcs:taxon:67589 should be saved",
                taxonService.find("urn:kew.org:wcs:taxon:67589"));
        assertEquals("urn:kew.org:wcs:taxon:71680 should have one child taxon",
                1, taxonService.find("urn:kew.org:wcs:taxon:71680")
                        .getChildren().size());

    }
}
