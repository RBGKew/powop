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
package org.emonocot.harvest.common;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.step.job.JobParametersExtractor;
import org.springframework.batch.item.ExecutionContext;

public class DefaultJobParametersExtractor implements JobParametersExtractor {

	private Set<String> keys = new HashSet<String>();

	private boolean useAllParentParameters = true;

	/**
	 * The key names to pull out of the execution context or job parameters, if
	 * they exist. If a key doesn't exist in the execution context then the job
	 * parameters from the enclosing job execution are tried, and if there is
	 * nothing there either then no parameter is extracted. Key names ending
	 * with <code>(long)</code>, <code>(int)</code>, <code>(double)</code>,
	 * <code>(date)</code> or <code>(string)</code> will be assumed to refer to
	 * values of the respective type and assigned to job parameters accordingly
	 * (there will be an error if they are not of the right type). Without a
	 * special suffix in that form a parameter is assumed to be of type String.
	 *
	 * @param keys the keys to set
	 */
	public void setKeys(String[] keys) {
		this.keys = new HashSet<String>(Arrays.asList(keys));
	}


	@Override
	public JobParameters getJobParameters(Job job, StepExecution stepExecution) {
		JobParametersBuilder builder = new JobParametersBuilder();
		Map<String, JobParameter> jobParameters = stepExecution.getJobParameters().getParameters();
		ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();
		ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
		if (useAllParentParameters) {
			for (String key : jobParameters.keySet()) {
				builder.addParameter(key, jobParameters.get(key));
			}
		}
		for (String key : keys) {
			if (key.endsWith("(long)")) {
				key = key.replace("(long)", "");
				if (jobExecutionContext.containsKey(key)) {
					builder.addLong(key, jobExecutionContext.getLong(key));
				}
				if (stepExecutionContext.containsKey(key)) {
					builder.addLong(key, stepExecutionContext.getLong(key));
				}
				else if (jobParameters.containsKey(key)) {
					builder.addLong(key, (Long) jobParameters.get(key).getValue());
				}
			}
			else if (key.endsWith("(int)")) {
				key = key.replace("(int)", "");
				if (jobExecutionContext.containsKey(key)) {
					builder.addLong(key, (long) jobExecutionContext.getInt(key));
				}
				if (stepExecutionContext.containsKey(key)) {
					builder.addLong(key, (long) stepExecutionContext.getInt(key));
				}
				else if (jobParameters.containsKey(key)) {
					builder.addLong(key, (Long) jobParameters.get(key).getValue());
				}
			}
			else if (key.endsWith("(double)")) {
				key = key.replace("(double)", "");
				if (jobExecutionContext.containsKey(key)) {
					builder.addDouble(key, jobExecutionContext.getDouble(key));
				}
				if (stepExecutionContext.containsKey(key)) {
					builder.addDouble(key, stepExecutionContext.getDouble(key));
				}
				else if (jobParameters.containsKey(key)) {
					builder.addDouble(key, (Double) jobParameters.get(key).getValue());
				}
			}
			else if (key.endsWith("(string)")) {
				key = key.replace("(string)", "");
				if (jobExecutionContext.containsKey(key)) {
					builder.addString(key, jobExecutionContext.getString(key));
				}
				if (stepExecutionContext.containsKey(key)) {
					builder.addString(key, stepExecutionContext.getString(key));
				}
				else if (jobParameters.containsKey(key)) {
					builder.addString(key, (String) jobParameters.get(key).getValue());
				}
			}
			else if (key.endsWith("(date)")) {
				key = key.replace("(date)", "");
				if (jobExecutionContext.containsKey(key)) {
					builder.addDate(key, (Date) jobExecutionContext.get(key));
				}
				if (stepExecutionContext.containsKey(key)) {
					builder.addDate(key, (Date) stepExecutionContext.get(key));
				}
				else if (jobParameters.containsKey(key)) {
					builder.addDate(key, (Date) jobParameters.get(key).getValue());
				}
			}
			else if (key.endsWith("(array)")){
				key = key.replace("(array)", "");
				if (jobExecutionContext.containsKey(key)) {
					builder.addString(key, Arrays.toString((String[])jobExecutionContext.get(key)));
				}
				if (stepExecutionContext.containsKey(key)) {
					builder.addString(key, Arrays.toString((String[])stepExecutionContext.get(key)));
				}
			}
			else {
				if (jobExecutionContext.containsKey(key)) {
					builder.addString(key, jobExecutionContext.get(key).toString());
				}
				if (stepExecutionContext.containsKey(key)) {
					builder.addString(key, stepExecutionContext.get(key).toString());
				}
				else if (jobParameters.containsKey(key)) {
					builder.addString(key, jobParameters.get(key).getValue().toString());
				}
			}
		}
		return builder.toJobParameters();
	}

}
