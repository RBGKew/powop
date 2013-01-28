package org.emonocot.harvest.integration;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.api.job.JobStatusNotifier;
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


/**
 *
 * @author ben
 *
 */
public class JobLauncherImpl implements JobLauncher {
	
	private org.springframework.batch.core.launch.JobLauncher jobLauncher;
	
	private JobLocator jobLocator;
	
	private JobStatusNotifier jobStatusNotifier;

	private String baseUrl;
	
	@Autowired
	public void setJobLauncher(org.springframework.batch.core.launch.JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}
	
	@Autowired
	public void setJobStatusNotifier(JobStatusNotifier jobStatusNotifier) {
		this.jobStatusNotifier = jobStatusNotifier;
	}

	/**
	 * @param baseUrl the baseUrl to set
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 *
	 * @param newJobLocator Set the job locator
	 */
	@Autowired
	public void setJobLocator(JobLocator newJobLocator) {
		this.jobLocator = newJobLocator;
	}

	@Override
	public void launch(JobLaunchRequest request) {
		Job job;
		try {
			job = jobLocator.getJob(request.getJob());
			Map<String, JobParameter> jobParameterMap = new HashMap<String,	JobParameter>();
			for(String parameterName : request.getParameters().keySet()) {
				jobParameterMap.put(parameterName, new JobParameter(request.getParameters().get(parameterName)));
			}
			JobParameters jobParameters = new JobParameters(jobParameterMap);
			try {				
				jobLauncher.run(job, jobParameters);				
			} catch (JobExecutionAlreadyRunningException jeare) {
				jobStatusNotifier.notify(new JobExecutionException(jeare.getLocalizedMessage()), request.getParameters().get("resource.identifier"));
			} catch (JobRestartException jre) {
				jobStatusNotifier.notify(new JobExecutionException(jre.getLocalizedMessage()), request.getParameters().get("resource.identifier"));
			} catch (JobInstanceAlreadyCompleteException jiace) {
				jobStatusNotifier.notify(new JobExecutionException(jiace.getLocalizedMessage()), request.getParameters().get("resource.identifier"));
			} catch (JobParametersInvalidException jpie) {
				jobStatusNotifier.notify(new JobExecutionException(jpie.getLocalizedMessage()), request.getParameters().get("resource.identifier"));
			}
		} catch (NoSuchJobException nsje) {
			jobStatusNotifier.notify(new JobExecutionException(nsje.getLocalizedMessage()), request.getParameters().get("resource.identifier"));			
		}
	}

}
