package org.emonocot.portal.integration;

import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLaunchResponse;
import org.emonocot.api.job.JobLauncher;


public class MockJobLauncher implements JobLauncher {
	
	private Object response = new JobLaunchResponse();
	
	private String request = null;


	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	@Override
	public JobExecutionInfo launch(JobLaunchRequest request)
			throws Exception {
		System.out.println("RECIEVED: " + request);
		return new JobExecutionInfo();
	}

	

}
