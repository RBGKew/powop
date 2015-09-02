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
package org.emonocot.job.gbif;

import java.io.File;
import java.util.UUID;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 *
 * @author ben
 *
 */
public class SetTemporaryFilenamesTasklet implements Tasklet {

	/**
	 *
	 */
	private String harvesterSpoolDirectory;

	/**
	 * @param newHarvesterSpoolDirectory the harvesterSpoolDirectory to set
	 */
	public final void setHarvesterSpoolDirectory(
			final String newHarvesterSpoolDirectory) {
		this.harvesterSpoolDirectory = newHarvesterSpoolDirectory;
	}

	/**
	 * @param contribution Set the step contribution
	 * @param chunkContext Set the chunk context
	 * @return the repeat status
	 * @throws Exception if there is a problem deleting the resources
	 */
	public final RepeatStatus execute(final StepContribution contribution,
			final ChunkContext chunkContext)
					throws Exception {
		UUID uuid1 = UUID.randomUUID();
		String temporaryFileName = harvesterSpoolDirectory + File.separator
				+ uuid1.toString() + ".xml";

		File temporaryFile = new File(temporaryFileName);
		ExecutionContext executionContext = chunkContext.getStepContext()
				.getStepExecution().getJobExecution().getExecutionContext();
		executionContext.put("startindex", 0);
		executionContext.put("temporary.file.name", temporaryFile.getAbsolutePath());
		executionContext.putLong("job.execution.id", chunkContext.getStepContext().getStepExecution().getJobExecutionId());
		return RepeatStatus.FINISHED;
	}
}
