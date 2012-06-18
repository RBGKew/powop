package org.emonocot.api.job;

import java.util.Map;

public class JobLaunchRequest {
	
	private String job;
	
	private Map<String,String> parameters;
	
	private JobExecutionInfo execution;
	
	private JobExecutionException exception;

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
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
	
}
