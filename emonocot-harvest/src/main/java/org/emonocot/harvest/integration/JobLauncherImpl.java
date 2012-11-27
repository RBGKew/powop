package org.emonocot.harvest.integration;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobInstanceInfo;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.joda.time.DateTime;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
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
	
	/**
	 *
	 */
	private org.springframework.batch.core.launch.JobLauncher jobLauncher;
	
	/**
	 *
	 */
	private JobLocator jobLocator;

	/**
	 *
	 */
	private String baseUrl;
	
	@Autowired
	public void setJobLauncher(org.springframework.batch.core.launch.JobLauncher newJobLauncher) {
		this.jobLauncher = newJobLauncher;
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
	public JobLaunchRequest launch(JobLaunchRequest request) {
		Job job;
		try {
			job = jobLocator.getJob(request.getJob());
			Map<String, JobParameter> jobParameterMap = new HashMap<String,	JobParameter>();
			for(String parameterName : request.getParameters().keySet()) {
				jobParameterMap.put(parameterName, new JobParameter(request.getParameters().get(parameterName)));
			}
			JobParameters jobParameters = new JobParameters(jobParameterMap);
			try {				
				JobExecution jobExecution = jobLauncher.run(job, jobParameters);
				JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
				DateTime startTime = new DateTime(jobExecution.getStartTime());
				DateTime endTime = new DateTime(jobExecution.getEndTime());
				jobExecutionInfo.setDuration(endTime.minus(startTime.getMillis()));
				jobExecutionInfo.setStartTime(startTime);
				jobExecutionInfo.setExitDescription(jobExecution.getExitStatus()
				         .getExitDescription());
				jobExecutionInfo.setExitCode(jobExecution.getExitStatus().getExitCode());
				jobExecutionInfo.setId(jobExecution.getId());
				JobInstanceInfo jobInstanceInfo = new JobInstanceInfo();
				jobInstanceInfo.setResource(baseUrl + "/jobs/"
				+ jobExecution.getJobInstance().getJobName() + "/"
				  + jobExecution.getJobId() + ".json");
				jobExecutionInfo.setJobInstance(jobInstanceInfo);
				jobExecutionInfo.setResource(baseUrl + "/jobs/executions/"
				 + jobExecution.getId() + ".json");
				jobExecutionInfo.setStatus(jobExecution.getStatus());
				request.setExecution(jobExecutionInfo);
			} catch (JobExecutionAlreadyRunningException jeare) {
				request.setException(new JobExecutionException(jeare.getLocalizedMessage()));
			} catch (JobRestartException jre) {
				request.setException(new JobExecutionException(jre.getLocalizedMessage()));
			} catch (JobInstanceAlreadyCompleteException jiace) {
				request.setException(new JobExecutionException(jiace.getLocalizedMessage()));
			} catch (JobParametersInvalidException jpie) {
				request.setException(new JobExecutionException(jpie.getLocalizedMessage()));
			}
		} catch (NoSuchJobException nsje) {
			request.setException(new JobExecutionException(nsje.getLocalizedMessage()));			
		}
		
		return request;
	}

}
