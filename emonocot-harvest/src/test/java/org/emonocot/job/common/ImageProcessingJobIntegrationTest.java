package org.emonocot.job.common;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author ben
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/META-INF/spring/batch/jobs/imageProcessing.xml",
		"/META-INF/spring/applicationContext-integration.xml",
		"/META-INF/spring/applicationContext-test.xml" })
public class ImageProcessingJobIntegrationTest {

	/**
     *
     */
	private Logger logger = LoggerFactory
			.getLogger(ImageProcessingJobIntegrationTest.class);

	/**
     *
     */
	@Autowired
	private JobLocator jobLocator;

	/**
     *
     */
	@Autowired
	@Qualifier("readWriteJobLauncher")
	private JobLauncher jobLauncher;

	@Before
	public void setUp() {
		String fullSizeImagesDirectoryName =  "./target/images/fullsize";
		File fullSizeImagesDirectory = new File(fullSizeImagesDirectoryName);
		fullSizeImagesDirectory.mkdirs();
		fullSizeImagesDirectory.deleteOnExit();
		String thumbnailImagesDirectoryName =  "./target/images/thumbnails";
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
		Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
		parameters.put("query.string", new JobParameter("select i from Image i"));

		JobParameters jobParameters = new JobParameters(parameters);

		Job job = jobLocator.getJob("ImageProcessing");
		assertNotNull("ImageProcessing must not be null", job);
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);

		for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
			logger.info(stepExecution.getStepName() + " "
					+ stepExecution.getReadCount() + " "
					+ stepExecution.getFilterCount() + " "
					+ stepExecution.getWriteCount());
		}
	}
}
