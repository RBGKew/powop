/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.job.dwc;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.emonocot.api.IdentificationKeyService;
import org.emonocot.api.ImageService;
import org.emonocot.api.PhylogeneticTreeService;
import org.emonocot.model.IdentificationKey;
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
public class DarwinCoreJobIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(DarwinCoreJobIntegrationTest.class);

    @Autowired
    private JobLocator jobLocator;

    @Autowired
	@Qualifier("readWriteJobLauncher")
    private JobLauncher jobLauncher;

    @Autowired
    private ImageService imageService;

    @Autowired
    private IdentificationKeyService identificationKeyService;

    @Autowired
    private PhylogeneticTreeService phylogeneticTreeService;

    private Properties properties;

    /**
     * 1288569600 in unix time.
     */
    private static final BaseDateTime PAST_DATETIME = new DateTime(2010, 11, 1, 9, 0, 0, 0);

    /**
     * @throws IOException 
     *
     */
    @Before
    public final void setUp() throws IOException {
        File imageDirectory = new File("./target/images/fullsize");
        imageDirectory.mkdirs();
        imageDirectory.deleteOnExit();
        File spoolDirectory = new File("./target/spool");
        spoolDirectory.mkdirs();
        spoolDirectory.deleteOnExit();
        File thumbnailDirectory = new File("./target/images/thumbnails");
        thumbnailDirectory.mkdirs();
        thumbnailDirectory.deleteOnExit();
        Resource propertiesFile = new ClassPathResource(
        "META-INF/spring/application.properties");
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
    public final void testNotModifiedResponse() throws IOException,
            NoSuchJobException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {
        Map<String, JobParameter> parameters =
            new HashMap<String, JobParameter>();
        parameters.put("authority.name", new JobParameter("test"));
        parameters.put("family", new JobParameter("Arecaceae"));
        String repository = properties.getProperty("test.resource.baseUrl");
        parameters.put("authority.uri", new JobParameter(repository + "dwc.zip"));
        parameters.put("authority.last.harvested",
                new JobParameter(Long.toString((DarwinCoreJobIntegrationTest.PAST_DATETIME.getMillis()))));
        parameters.put("phylogeny.processing.mode", new JobParameter("IMPORT_PHYLOGENIES_DONT_DELETE_GLOBAL"));
        parameters.put("key.processing.mode", new JobParameter("IMPORT_KEYS_DONT_DELETE_GLOBAL"));
        JobParameters jobParameters = new JobParameters(parameters);

        Job darwinCoreArchiveHarvestingJob = jobLocator.getJob("DarwinCoreArchiveHarvesting");
        assertNotNull("DarwinCoreArchiveHarvesting must not be null", darwinCoreArchiveHarvestingJob);
        JobExecution jobExecution = jobLauncher.run(darwinCoreArchiveHarvestingJob, jobParameters);
        assertEquals("The job should complete successfully", "COMPLETED", jobExecution.getExitStatus().getExitCode());
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
        	logger.info(stepExecution.getStepName() + " "
                    + stepExecution.getReadCount() + " "
                    + stepExecution.getFilterCount() + " "
                    + stepExecution.getWriteCount() + " " + stepExecution.getCommitCount());
        }
        logger.info(jobExecution.getExitStatus().getExitCode() + " | " + jobExecution.getExitStatus().getExitDescription());

        assertNotNull("The image in the image file should have been persisted", imageService.load("http://media.e-taxonomy.eu/palmae/photos/palm_tc_170762_1.jpg"));
        assertNotNull("The image in the multimedia file should have been persisted", imageService.load("http://media.e-taxonomy.eu/palmae/photos/palm_tc_170762_8.jpg"));
        //This is a slightly fragile assertion as it depends on a fixed location of the test resource.
        //I couldn't find a way of using the "test.resources.baseUrl" in the URL of the phylogeny and ID Key as the data is in a pre-packaged zip file (dwc.zip).
        assertNotNull("The phylogeny in the multimedia file should have been persisted", phylogeneticTreeService.load(repository + "1_1326150157_Strelitziaceae_Cron.nexorg"));

        IdentificationKey localKey = null;
        try {
            localKey = identificationKeyService.load(repository + "European_Pontederiaceae.xml");
        } catch (Exception e) {}//Prefer test failure than a test error
        assertNotNull("The key in the image file should have been persisted but was :" + localKey, localKey);
    }
}
