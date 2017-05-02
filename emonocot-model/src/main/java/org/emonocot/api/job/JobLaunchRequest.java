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
package org.emonocot.api.job;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.JobConfiguration;

import lombok.Data;

@Data
public class JobLaunchRequest {

	private String job;

	private Map<String,String> parameters;

	private JobExecutionInfo execution;

	private JobExecutionException exception;

	public JobLaunchRequest() { }

	public JobLaunchRequest(JobConfiguration conf) {
		this.job = conf.getJobName();
		this.parameters = conf.getParameters();
	}

	public void addParameter(String key, String value) {
		if(parameters == null) {
			parameters = new HashMap<>();
		}
		parameters.put(key, value);
	}
}
