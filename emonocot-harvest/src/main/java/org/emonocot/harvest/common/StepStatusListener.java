package org.emonocot.harvest.common;

import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobStatusNotifier;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class StepStatusListener implements StepExecutionListener {
	
	private JobStatusNotifier jobStatusNotifier;
	
	private String baseUrl;
	
	public void setJobStatusNotifier(JobStatusNotifier jobStatusNotifier) {
		this.jobStatusNotifier = jobStatusNotifier;
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		JobExecution jobExecution = stepExecution.getJobExecution();
		if (jobExecution.getJobInstance().getJobParameters().getString("resource.identifier") != null) {
            JobExecutionInfo jobExecutionInfo = new JobExecutionInfo(jobExecution, baseUrl);
			jobStatusNotifier.notify(jobExecutionInfo);
		}
		return null;
	}

}
