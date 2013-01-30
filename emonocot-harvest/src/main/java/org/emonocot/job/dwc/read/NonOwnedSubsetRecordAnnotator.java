package org.emonocot.job.dwc.read;

import org.emonocot.model.hibernate.OlapDateTimeUserType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;


public class NonOwnedSubsetRecordAnnotator extends AbstractRecordAnnotator implements Tasklet {
	private Logger logger = LoggerFactory.getLogger(NonOwnedSubsetRecordAnnotator.class);
	
    private String authorityName;
	
	private String subtribe;
	
	private String tribe;
	
	private String subfamily;
	
	private String family;

	private String queryString;

	private String annotatedObjType;

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

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
		Integer authorityId = jdbcTemplate.queryForInt("Select id from Organisation where identifier = '" + authorityName + "'");
	    stepExecution.getJobExecution().getExecutionContext().putLong("job.execution.id", stepExecution.getJobExecutionId());
	      
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
	      
	    queryString = queryString.replaceAll(":authorityId", authorityId.toString());
	    if(subsetValue != null) {
	      queryString = queryString.replaceAll(":subsetRank", subsetRank);
          queryString = queryString.replaceAll(":subsetValue", subsetValue);
	    }
	    queryString = queryString.replaceAll(":jobId", stepExecution.getJobExecutionId().toString());
	    queryString = queryString.replaceAll(":dateTime", OlapDateTimeUserType.convert(new DateTime()).toString());
	    queryString = queryString.replaceAll(":annotatedObjType", annotatedObjType);
	    logger.info(queryString);
	    jdbcTemplate.update(queryString);
		return RepeatStatus.FINISHED;
	}
}
