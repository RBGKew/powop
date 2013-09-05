package org.emonocot.job.palmweb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "/META-INF/spring/batch/jobs/palmweb.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@Ignore
public class PalmwebIntegrationTest {
	
	private Logger logger = LoggerFactory.getLogger(PalmwebIntegrationTest.class);
		
	 @Autowired
	 private JobLocator jobLocator;

	 @Autowired
	 private JobLauncher jobLauncher;
	
	@Test
	public void testCreateGenericArchive() throws NoSuchJobException,
			JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException, IOException {
		 Map<String, JobParameter> parameters =
	            new HashMap<String, JobParameter>();
	   
	    JobParameters jobParameters = new JobParameters(parameters);

	    Job palmwebArchive = jobLocator.getJob("PalmWeb");
	    assertNotNull("Palmweb must not be null",  palmwebArchive);
	    JobExecution jobExecution = jobLauncher.run(palmwebArchive, jobParameters);
	    assertEquals("The job should complete successfully",jobExecution.getExitStatus().getExitCode(),"COMPLETED");
	    for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
	        logger.info(stepExecution.getStepName() + " "
	                    + stepExecution.getReadCount() + " "
	                    + stepExecution.getFilterCount() + " "
	                    + stepExecution.getWriteCount() + " " + stepExecution.getCommitCount());
	   }
	}

}
