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
package org.emonocot.job.dwc;

import java.io.File;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class SetTemporaryFilenamesTasklet implements Tasklet {

	private final Logger log = LoggerFactory.getLogger(SetTemporaryFilenamesTasklet.class);

	private String harvesterSpoolDirectory;

	public final void setHarvesterSpoolDirectory(final String newHarvesterSpoolDirectory) {
		this.harvesterSpoolDirectory = newHarvesterSpoolDirectory;
	}

	/**
	 * @param contribution Set the step contribution
	 * @param chunkContext Set the chunk context
	 * @return the repeat status
	 * @throws Exception if there is a problem deleting the resources
	 */
	public final RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
		UUID uuid = UUID.randomUUID();
		String unpackDirectoryName = harvesterSpoolDirectory + File.separator + uuid.toString();
		String temporaryFileName = harvesterSpoolDirectory + File.separator + uuid.toString() + ".zip";

		File unpackDirectory = new File(unpackDirectoryName);
		unpackDirectory.mkdir();
		File temporaryFile = new File(temporaryFileName);
		ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		executionContext.put("temporary.file.name", temporaryFile.getAbsolutePath());
		executionContext.put("unpack.directory.name", unpackDirectory.getAbsolutePath());
		executionContext.putLong("job.execution.id", chunkContext.getStepContext().getStepExecution().getJobExecutionId());
		log.debug("setting temporary.file.name to {}", temporaryFile.getAbsolutePath());
		log.debug("execution context: {}", executionContext);
		return RepeatStatus.FINISHED;
	}
}
