package org.emonocot.job.dwc.taxon;


import org.emonocot.job.dwc.read.AbstractRecordAnnotator;
import org.emonocot.model.hibernate.OlapDateTimeUserType;
import org.joda.time.DateTime;
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

	private String authorityName;
	
	private String subtribe;
	
	private String tribe;
	
	private String subfamily;
	
	private String family;

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

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Integer authorityId = jdbcTemplate.queryForInt("Select id from Organisation where identifier = '" + authorityName + "'");
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
	    String queryString = "insert into Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, type, code, recordType) select t.id, 'Taxon', ':jobId', :dateTime, :authorityId, 'Warn', 'Absent', 'Taxon' from Taxon t left join Taxon a on (t.acceptedNameUsage_id = a.id) where (t.:subsetRank = ':subsetValue' or a.:subsetRank = ':subsetValue')";
	    stepExecution.getJobExecution().getExecutionContext().putLong("job.execution.id", stepExecution.getJobExecutionId());
	    queryString = queryString.replaceAll(":authorityId", authorityId.toString());
	    queryString = queryString.replaceAll(":jobId", stepExecution.getJobExecutionId().toString());
	    queryString = queryString.replaceAll(":dateTime", OlapDateTimeUserType.convert(new DateTime()).toString());
	    queryString = queryString.replaceAll(":subsetRank", subsetRank);
	    queryString = queryString.replaceAll(":subsetValue", subsetValue);
	    logger.error(queryString);
	    int numberOfRecords = jdbcTemplate.update(queryString);
	    logger.error(numberOfRecords + " records inserted");
		return RepeatStatus.FINISHED;
	}
}
