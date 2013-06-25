package org.emonocot.harvest.integration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CompositeJobStatusListener implements JobExecutionListener {
	
	private List<JobExecutionListener> jobListeners = new ArrayList<JobExecutionListener>();

	public void setJobListeners(List<JobExecutionListener> jobListeners) {
		this.jobListeners = jobListeners;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		for(JobExecutionListener jobListener : jobListeners) {
			jobListener.beforeJob(jobExecution);
		}
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		for(JobExecutionListener jobListener : jobListeners) {
			jobListener.afterJob(jobExecution);
		}
	}

}
