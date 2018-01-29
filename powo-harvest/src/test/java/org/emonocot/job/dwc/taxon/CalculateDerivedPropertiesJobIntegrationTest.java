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
package org.emonocot.job.dwc.taxon;

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

/**
 *
 * @author ben
 *
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"/META-INF/spring/batch/jobs/calculateDerived.xml",
	"/META-INF/spring/applicationContext-integration.xml",
"/META-INF/spring/applicationContext-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CalculateDerivedPropertiesJobIntegrationTest {

	/**
	 *
	 */
	private Logger logger = LoggerFactory.getLogger(
			CalculateDerivedPropertiesJobIntegrationTest.class);

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
		parameters.put("query.string", new JobParameter(
				"select t.id from Taxon t join t.parentNameUsage"));

		JobParameters jobParameters = new JobParameters(parameters);

		Job job = jobLocator
				.getJob("CalculateDerivedProperties");
		assertNotNull("CalculateDerivedProperties must not be null",
				job);
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		assertEquals("The job should complete successfully",jobExecution.getExitStatus().getExitCode(),"COMPLETED");

		for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
			logger.info(stepExecution.getStepName() + " "
					+ stepExecution.getReadCount() + " "
					+ stepExecution.getFilterCount() + " "
					+ stepExecution.getWriteCount());
		}
	}
}
