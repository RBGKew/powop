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

/**
 *
 * @author ben
 *
 */
public class OwnedRecordAnnotator extends AbstractRecordAnnotator implements Tasklet {

	private Logger logger = LoggerFactory.getLogger(OwnedRecordAnnotator.class);

	private String subtribe;

	private String tribe;

	private String subfamily;

	private String family;

	private String annotatedObjType;

	public void setSubtribe(String subtribe) {
		this.subtribe = subtribe;
	}

	public void setTribe(String tribe) {
		this.tribe = tribe;
	}

	public void setSubfamily(String subfamily) {
		this.subfamily = subfamily;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public void setAnnotatedObjType(String annotatedObjType) {
		this.annotatedObjType = annotatedObjType;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		String subsetRank = null;
		String subsetValue = null;

		if (subtribe != null) {
			subsetRank = "subtribe";
			subsetValue = subtribe;
		} else if (tribe != null) {
			subsetRank = "tribe";
			subsetValue = tribe;
		} else if (subfamily != null) {
			subsetRank = "subfamily";
			subsetValue = subfamily;
		} else if (family != null) {
			subsetRank = "family";
			subsetValue = family;
		}

		String queryString = null;
		Map<String, Object> queryParameters = new HashMap<String, Object>();

		if (subsetValue != null) {
			queryString = "INSERT INTO Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, type, code, recordType) "
					+ "SELECT o.id, :annotatedObjType, :jobId, now(), :authorityId, 'Warn', 'Absent', :annotatedObjType "
					+ "FROM #annotatedObjType o "
					+ "JOIN TAXON t ON (o.taxon_id = t.id) "
					+ "LEFT JOIN taxon a ON (t.acceptedNameUsage_id = a.id) "
					+ "WHERE o.authority_id = :authorityId AND (t.#subsetRank = :subsetValue or a.#subsetRank = :subsetValue)";
			queryString = queryString.replaceAll("#subsetRank", subsetRank);
			queryParameters.put("subsetValue", subsetValue);
		} else {
			queryString = "INSERT INTO Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, type, code, recordType) "
					+ "SELECT o.id, :annotatedObjType, :jobId, now(), :authorityId, 'Warn', 'Absent', :annotatedObjType "
					+ "FROM #annotatedObjType o "
					+ "WHERE o.authority_id = :authorityId";
		}

		queryString = queryString.replaceAll("#annotatedObjType", annotatedObjType);
		queryParameters.put("annotatedObjType", annotatedObjType);

		logger.debug(queryString);
		int numberUpdated = annotate(queryString, queryParameters);
		logger.debug(numberUpdated + " Annotation records inserted");
		return RepeatStatus.FINISHED;
	}
}
