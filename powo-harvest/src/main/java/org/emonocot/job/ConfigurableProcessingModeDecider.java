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
package org.emonocot.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class ConfigurableProcessingModeDecider implements JobExecutionDecider {

	Logger logger = LoggerFactory.getLogger(ConfigurableProcessingModeDecider.class);

	private String processingModeKey = null;

	private String defaultProcessingMode = null;

	/**
	 * @param processingModeKey the processingModeKey to set
	 */
	public void setProcessingModeKey(String processingModeKey) {
		this.processingModeKey = processingModeKey;
	}

	/**
	 * @param defaultProcessingMode the defaultProcessingMode to set
	 */
	public void setDefaultProcessingMode(String defaultProcessingMode) {
		this.defaultProcessingMode = defaultProcessingMode;
	}

	/**
	 * @param jobExecution set the job execution
	 * @param stepExecution set the step execution
	 * @return FlowExecutionStatus a status
	 */
	public final FlowExecutionStatus decide(final JobExecution jobExecution, final StepExecution stepExecution) {
		if(processingModeKey == null && defaultProcessingMode == null) {
			logger.error("No processing mode was found.  Unable to continue", new IllegalArgumentException("A processing mode must exist if specified"));
			return FlowExecutionStatus.FAILED;
		}

		if (jobExecution.getExecutionContext().containsKey(processingModeKey)) {
			return new FlowExecutionStatus(jobExecution.getExecutionContext().getString(processingModeKey));
		} else if(jobExecution.getJobParameters().getParameters().containsKey(processingModeKey)) {
			return new FlowExecutionStatus(jobExecution.getJobParameters().getString(processingModeKey));
		} else {
			return new FlowExecutionStatus(defaultProcessingMode);
		}
	}
}
