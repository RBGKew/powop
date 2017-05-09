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
package org.emonocot.harvest.common;

import org.emonocot.model.JobConfiguration;
import org.emonocot.service.impl.JobConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class NotifyingJobStatusListener extends JobExecutionListenerSupport {

	private static Logger logger = LoggerFactory.getLogger(NotifyingJobStatusListener.class);

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JobConfigurationService jobConfigurationService;

	public NotifyingJobStatusListener() { }

	@Override
	public void afterJob(JobExecution jobExecution) {

		StringBuffer exitDescription = new StringBuffer();

		if (jobExecution.getJobParameters().getString("job.configuration.id") != null) {
			JobConfiguration jobConfiguration = jobConfigurationService.get(Long.parseLong(jobExecution.getJobParameters().getString("job.configuration.id")));
			exitDescription.append("Harvested " + jobConfiguration.getDescription() + " " + jobExecution.getExitStatus().getExitCode());
			exitDescription.append(". " + jobExecution.getStepExecutions().size() + " Steps Completed.");
			jobExecution.setExitStatus(jobExecution.getExitStatus().addExitDescription(exitDescription.toString()));

			jobRepository.update(jobExecution);

			logger.info(jobExecution.getExitStatus().getExitCode() + " " + jobExecution.getExitStatus().getExitDescription());
		} else {
			exitDescription.append(jobExecution.getJobInstance().getJobName()  + " " + jobExecution.getExitStatus().getExitCode());
			exitDescription.append(". " + jobExecution.getStepExecutions().size() + " Steps Completed.");
			jobExecution.setExitStatus(new ExitStatus(jobExecution.getExitStatus().getExitCode(),exitDescription.toString()));
			jobRepository.update(jobExecution);

			logger.info(jobExecution.getExitStatus().getExitCode() + " " + jobExecution.getExitStatus().getExitDescription());
		}
	}
}
