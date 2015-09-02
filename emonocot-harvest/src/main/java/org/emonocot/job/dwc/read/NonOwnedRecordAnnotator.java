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
package org.emonocot.job.dwc.read;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;


public class NonOwnedRecordAnnotator extends AbstractRecordAnnotator implements Tasklet {
	private Logger logger = LoggerFactory.getLogger(NonOwnedRecordAnnotator.class);

	private String annotatedObjType;

	public void setAnnotatedObjType(String annotatedObjType) {
		this.annotatedObjType = annotatedObjType;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		String annotationQuery = "insert into Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, type, code, recordType) select o.id, :annotatedObjType, :jobId, now(), :authorityId, 'Warn', 'Absent', :annotatedObjType from #annotatedObjType o where o.authority_id = :authorityId";
		annotationQuery = annotationQuery.replaceAll("#annotatedObjType", annotatedObjType);
		Map<String,Object> annotationParameters = new HashMap<String,Object>();
		annotationParameters.put("annotatedObjType", annotatedObjType);
		super.annotate(annotationQuery, annotationParameters);
		return RepeatStatus.FINISHED;
	}
}
