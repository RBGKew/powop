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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ParameterConvertingTasklet implements Tasklet {

	private final Logger logger = LoggerFactory.getLogger(ParameterConvertingTasklet.class);

	private String fieldNames;

	public final void setFieldNames(final String fieldNames) {
		this.fieldNames = fieldNames;
	}

	private String defaultValues;

	private String fieldNamesKey;

	private String defaultValuesKey;

	public final void setFieldNamesKey(String fieldNamesKey) {
		this.fieldNamesKey = fieldNamesKey;
	}

	public final void setDefaultValuesKey(String defaultValuesKey) {
		this.defaultValuesKey = defaultValuesKey;
	}

	public final void setDefaultValues(final String defaultValues) {
		this.defaultValues = defaultValues;
	}

	public final RepeatStatus execute(final StepContribution contribution,
			final ChunkContext chunkContext) throws Exception {
		Map<String,String> defaultValuesMap = new HashMap<String,String>();
		if(this.defaultValues != null) {
			String values = defaultValues.substring(1, this.defaultValues.length() - 1);
			for(String defaultValue : values.split(",")) {
				if(defaultValue.indexOf("=") > -1) {
					String field = defaultValue.substring(0,defaultValue.indexOf("="));
					String value = defaultValue.substring(defaultValue.indexOf("=") + 1, defaultValue.length());
					defaultValuesMap.put(field,value);
				}
			}
		}
		chunkContext.getStepContext().getStepExecution()
		.getJobExecution().getExecutionContext().put(defaultValuesKey, defaultValuesMap);
		logger.debug("SETTING " + defaultValuesKey + " as " + defaultValuesMap);
		String names = fieldNames.substring(1, this.fieldNames.length() - 1);
		String[] fieldNamesArray = names.split(",");
		chunkContext.getStepContext().getStepExecution()
		.getJobExecution().getExecutionContext().put(fieldNamesKey, fieldNamesArray);
		logger.debug("SETTING " + fieldNamesKey + " as " + Arrays.toString(fieldNamesArray));
		return RepeatStatus.FINISHED;
	}
}
