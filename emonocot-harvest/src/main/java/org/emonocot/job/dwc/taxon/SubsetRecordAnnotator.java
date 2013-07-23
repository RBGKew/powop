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
public class SubsetRecordAnnotator extends AbstractRecordAnnotator implements Tasklet { 
	
	Logger logger = LoggerFactory.getLogger(SubsetRecordAnnotator.class);
	
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
	    String queryString = "insert into Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, type, code, recordType) select t.id, 'Taxon', :jobId, now(), :authorityId, 'Warn', 'Absent', 'Taxon' from Taxon t left join Taxon a on (t.acceptedNameUsage_id = a.id) where t.authority_id = :authorityId and (t.#subsetRank = :subsetValue or a.#subsetRank = :subsetValue)";
	    queryString = queryString.replaceAll("#subsetRank", subsetRank);
	    Map<String, Object> queryParameters = new HashMap<String,Object>();
	    queryParameters.put("subsetValue", subsetValue);
	    logger.debug(queryString);
	    int numberOfRecords = annotate(queryString, queryParameters);
	    logger.debug(numberOfRecords + " records inserted");
		return RepeatStatus.FINISHED;
	}
}
