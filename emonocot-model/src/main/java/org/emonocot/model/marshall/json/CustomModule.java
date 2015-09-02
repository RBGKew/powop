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
package org.emonocot.model.marshall.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleKeyDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import org.emonocot.api.JobInstanceService;
import org.emonocot.model.constants.Location;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobInstance;

/**
 *
 * @author ben
 *
 */
public class CustomModule extends Module {

	/**
	 *
	 */
	private JobInstanceService jobInstanceService;

	/**
	 *
	 * @param newJobInstanceService Set the job instance service
	 */
	public CustomModule(final JobInstanceService newJobInstanceService) {
		this.jobInstanceService = newJobInstanceService;
	}

	@Override
	public final String getModuleName() {
		return "eMonocotModule";
	}

	@Override
	public final void setupModule(final SetupContext setupContext) {
		SimpleKeyDeserializers keyDeserializers = new SimpleKeyDeserializers();
		keyDeserializers.addDeserializer(Location.class,
				new GeographicalRegionKeyDeserializer());
		setupContext.addKeyDeserializers(keyDeserializers);
		SimpleSerializers simpleSerializers = new SimpleSerializers();
		simpleSerializers.addSerializer(new JobInstanceSerializer());
		simpleSerializers.addSerializer(new JobExecutionSerializer());
		setupContext.addSerializers(simpleSerializers);

		SimpleDeserializers simpleDeserializers = new SimpleDeserializers();
		simpleDeserializers.addDeserializer(JobInstance.class,
				new JobInstanceDeserializer());
		simpleDeserializers.addDeserializer(JobExecution.class,
				new JobExecutionDeserializer(jobInstanceService));
		simpleDeserializers.addDeserializer(JobExecutionException.class,
				new JobExecutionExceptionDeserializer());
		setupContext.addDeserializers(simpleDeserializers);
	}

	@Override
	public final Version version() {
		return new Version(0, 1, 0, "alpha");
	}

}
