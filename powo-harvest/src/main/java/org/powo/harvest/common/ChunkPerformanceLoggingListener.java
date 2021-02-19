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
package org.powo.harvest.common;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChunkPerformanceLoggingListener implements ChunkListener, StepExecutionListener {

	private int chunkCount = 0;
	private long startMillis;
	private Logger logger = LoggerFactory.getLogger(ChunkPerformanceLoggingListener.class);

	@Override
	public void beforeChunk(ChunkContext context) {
		startMillis = System.currentTimeMillis();
	}

	@Override
	public void afterChunk(ChunkContext context) {
		chunkCount++;
		var millis = System.currentTimeMillis() - startMillis;
		logger.info("Processed chunk " + chunkCount + " in " + millis + "ms");
	}

	@Override
	public void afterChunkError(ChunkContext context) {
		chunkCount++;
		var millis = System.currentTimeMillis() - startMillis;
		logger.info("Chunk " + chunkCount + " errored after " + millis + "ms");
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		chunkCount = 0;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
