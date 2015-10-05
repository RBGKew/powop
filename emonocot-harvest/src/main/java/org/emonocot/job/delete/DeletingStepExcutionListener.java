package org.emonocot.job.delete;

import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobStatusNotifier;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class DeletingStepExcutionListener implements StepExecutionListener{

	private JobStatusNotifier jobStatusNotifier;
	private String baseUrl;

	public void setJobStatusNotifier(JobStatusNotifier jobStatusNotifier) {
		this.jobStatusNotifier = jobStatusNotifier;
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	@Override
	public ExitStatus afterStep(StepExecution step) {
		ExitStatus exitStatus = new ExitStatus("RECORDS DELETED");
		exitStatus.addExitDescription("All records deleted. press delete again to remove resource");
		JobExecution jobExecution = step.getJobExecution();
		if (jobExecution.getJobInstance().getJobParameters().getString("resource.identifier") != null) {
			JobExecutionInfo jobExecutionInfo = new JobExecutionInfo(jobExecution, baseUrl);
			jobStatusNotifier.notify(jobExecutionInfo);
		}
		return exitStatus;

	}

	@Override
	public void beforeStep(StepExecution begin) {

		
	}
	
}	
