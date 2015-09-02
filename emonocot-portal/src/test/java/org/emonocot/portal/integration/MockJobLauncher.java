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
package org.emonocot.portal.integration;

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
