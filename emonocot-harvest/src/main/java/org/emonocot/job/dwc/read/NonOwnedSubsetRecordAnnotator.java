package org.emonocot.job.dwc.read;



import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;


public class NonOwnedSubsetRecordAnnotator extends AbstractRecordAnnotator implements Tasklet {
	
	private Logger logger = LoggerFactory.getLogger(NonOwnedSubsetRecordAnnotator.class);
	
	private String subtribe;
	
	private String tribe;
	
	private String subfamily;
	
	private String family;

	private String queryString;

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

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public void setAnnotatedObjType(String annotatedObjType) {
		this.annotatedObjType = annotatedObjType;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution,	ChunkContext chunkContext) throws Exception {
	      
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
	    } else if(family != null) {
	    	subsetRank = "family";
	    	subsetValue = family;
	    }	      
	   
	    Map<String,Object> annotationParameters = new HashMap<String,Object>();
	    queryString = queryString.replaceAll("#subsetRank", subsetRank);
	    annotationParameters.put("subsetValue", subsetValue);
	    annotationParameters.put("annotatedObjType", annotatedObjType);
        super.annotate(queryString, annotationParameters);
		return RepeatStatus.FINISHED;
	}
}
