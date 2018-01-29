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
package org.powo.api.job;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.powo.model.marshall.json.DateTimeSerializer;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
@Component
public class JobExecutionInfo implements Serializable {

	private static final long serialVersionUID = 7716817740821985536L;

	private String resource;

	private Long id;

	private BatchStatus status;

	@JsonSerialize(using = DateTimeSerializer.class)
	private DateTime startTime;

	private String exitCode;

	@JsonSerialize(using = DateTimeSerializer.class)
	private DateTime duration;

	private String exitDescription;

	private String jobInstance;

	private Integer recordsRead = 0;

	private Integer readSkip = 0;

	private Integer processSkip = 0;

	private Integer writeSkip = 0;

	private Integer written = 0;

	private String jobConfigurationId;

	private Integer progress;

	public JobExecutionInfo() { }

	public JobExecutionInfo(JobExecution jobExecution) {
		DateTime sTime = new DateTime(jobExecution.getStartTime());
		DateTime eTime = new DateTime(jobExecution.getEndTime());
		duration = eTime.minus(sTime.getMillis());
		startTime = sTime;
		exitDescription = jobExecution.getExitStatus().getExitDescription();
		exitCode = jobExecution.getExitStatus().getExitCode();
		id = jobExecution.getId();
		status = jobExecution.getStatus();
		jobConfigurationId = jobExecution.getJobParameters().getString("job.configuration.id");

		Integer writeSkip = 0;
		for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
			recordsRead += stepExecution.getReadCount();
			readSkip += stepExecution.getReadSkipCount();
			processSkip += stepExecution.getProcessSkipCount();
			written += stepExecution.getWriteCount();
			writeSkip += stepExecution.getWriteSkipCount();
		}
	}
}
