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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChunkPerformanceLoggingListener implements ChunkListener, StepExecutionListener {

	private Logger logger = LoggerFactory.getLogger(ChunkPerformanceLoggingListener.class);

	private AtomicInteger chunkCount = new AtomicInteger(1);
	private Map<Integer, Long> chunkTimes = new ConcurrentHashMap<>();

	@Override
	public void beforeChunk(ChunkContext context) {
		var chunkTimingId = chunkCount.getAndIncrement();
		context.setAttribute("chunkTimingId", chunkTimingId);
		chunkTimes.put(chunkTimingId, System.currentTimeMillis());
	}

	@Override
	public void afterChunk(ChunkContext context) {
		var chunkTimingId = (Integer) context.getAttribute("chunkTimingId");
		var startMillis = chunkTimes.get(chunkTimingId);
		var millis = System.currentTimeMillis() - startMillis;		
		logger.info("[{}] chunk {} processed in {}ms", context.getStepContext().getStepName(),  chunkTimingId, millis);
	}

	@Override
	public void afterChunkError(ChunkContext context) {
		var chunkTimingId = (Integer) context.getAttribute("chunkTimingId");
		var startMillis = chunkTimes.get(chunkTimingId);
		var millis = System.currentTimeMillis() - startMillis;
		logger.error("[{}] chunk {} errored in {}ms", context.getStepContext().getStepName(),  chunkTimingId, millis);
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		chunkCount.set(1);
		chunkTimes.clear();
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
