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
    "/org/emonocot/job/palmweb/palmweb.xml" })
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
