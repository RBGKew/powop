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
package org.powo.harvest.service;

import java.util.HashMap;
import java.util.Map;

import org.powo.api.job.JobExecutionException;
import org.powo.api.job.JobLaunchRequest;
import org.powo.api.job.JobLauncher;
import org.powo.api.job.JobStatusNotifier;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JobLauncherImpl implements JobLauncher {

	private org.springframework.batch.core.launch.JobLauncher jobLauncher;

	private JobLocator jobLocator;

	private JobStatusNotifier jobStatusNotifier;

	@Autowired
	public void setJobLauncher(@Qualifier("asynchronousJobLauncher") org.springframework.batch.core.launch.JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	@Autowired
	public void setJobStatusNotifier(JobStatusNotifier jobStatusNotifier) {
		this.jobStatusNotifier = jobStatusNotifier;
	}

	@Autowired
	public void setJobLocator(JobLocator newJobLocator) {
		this.jobLocator = newJobLocator;
	}

	@Override
	public void launch(JobLaunchRequest request) {
		Job job;
		try {
			job = jobLocator.getJob(request.getJob());
			Map<String, JobParameter> jobParameterMap = new HashMap<String, JobParameter>();
			for(String parameterName : request.getParameters().keySet()) {
				jobParameterMap.put(parameterName, new JobParameter(request.getParameters().get(parameterName)));
			}

			JobParameters jobParameters = new JobParameters(jobParameterMap);
			jobLauncher.run(job, jobParameters);
		} catch (NoSuchJobException 
				| JobExecutionAlreadyRunningException
				| JobRestartException
				| JobInstanceAlreadyCompleteException
				| JobParametersInvalidException exception) {
				jobStatusNotifier.notify(new JobExecutionException(exception.getLocalizedMessage()), request.getParameters().get("job.configuration.id"));
		}
	}
}
