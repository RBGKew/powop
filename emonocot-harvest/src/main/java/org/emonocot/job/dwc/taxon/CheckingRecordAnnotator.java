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
package org.emonocot.job.dwc.taxon;


import java.util.HashMap;
import java.util.Map;

import org.emonocot.job.dwc.read.AbstractRecordAnnotator;
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
public class CheckingRecordAnnotator extends AbstractRecordAnnotator implements Tasklet { 
	
	Logger logger = LoggerFactory.getLogger(CheckingRecordAnnotator.class);
	
	private String subtribe;
	
	private String tribe;
	
	private String subfamily;
	
	private String family;

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

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
	    String subsetRank = null;
	    String subsetValue = null;
	      
	    if(subtribe != null) {
	    	subsetRank = "subtribe";
	    	subsetValue = subtribe;
	    } else if(tribe != null) {
	    	subsetRank = "tribe";
	    	subsetValue = tribe;
	    } else if(subfamily != null) {
	    	subsetRank = "subfamily";
	    	subsetValue = subfamily;
	    } else {
	    	subsetRank = "family";
	    	subsetValue = family;
	    }
	    String queryString = "insert into Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, type, code, recordType) select t.id, 'Taxon', :jobId, now(), :authorityId, 'Warn', 'Absent', 'Taxon' from Taxon t left join Taxon a on (t.acceptedNameUsage_id = a.id) where (t.#subsetRank = :subsetValue or a.#subsetRank = :subsetValue)";
	    queryString = queryString.replaceAll("#subsetRank", subsetRank);
	    Map<String, Object> queryParameters = new HashMap<String,Object>();
	    queryParameters.put("subsetValue", subsetValue);
	    logger.debug(queryString);
	    int numberOfRecords = annotate(queryString, queryParameters);
	    logger.debug(numberOfRecords + " records inserted");
		return RepeatStatus.FINISHED;
	}
}
