package org.emonocot.integration;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MockJobLauncher implements JobLauncher {
	
	private static Logger logger = LoggerFactory.getLogger(MockJobLauncher.class);
	
	private JobExecutionInfo execution = new JobExecutionInfo();
	
	private JobExecutionException exception = null;
	
	private JobLaunchRequest request = null;

	public JobLaunchRequest getRequest() {
		return request;
	}

	public void setRequest(JobLaunchRequest request) {
		this.request = request;
	}

	/**
	 * @return the execution
	 */
	public JobExecutionInfo getExecution() {
		return execution;
	}

	/**
	 * @param execution the execution to set
	 */
	public void setExecution(JobExecutionInfo execution) {
		this.execution = execution;
	}

	/**
	 * @return the exception
	 */
	public JobExecutionException getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(JobExecutionException exception) {
		this.exception = exception;
	}

	@Override
	public void launch(JobLaunchRequest request)
			throws JobExecutionException {
		logger.info("RECIEVED: " + request);
		request.setException(exception);
		request.setExecution(execution);
	}
}
