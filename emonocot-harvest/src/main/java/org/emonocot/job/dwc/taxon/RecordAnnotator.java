package org.emonocot.job.dwc.taxon;


import org.emonocot.job.dwc.read.AbstractRecordAnnotator;
import org.emonocot.model.hibernate.OlapDateTimeUserType;
import org.joda.time.DateTime;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 *
 * @author ben
 *
 */
public class RecordAnnotator extends AbstractRecordAnnotator implements Tasklet { 

	private String authorityName;

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Integer authorityId = jdbcTemplate.queryForInt("Select id from Organisation where identifier = '" + authorityName + "'");
	     
	    String queryString = "insert into Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, type, code, recordType) select t.id, 'Taxon', ':jobId', :dateTime, :authorityId, 'Warn', 'Absent', 'Taxon' from Taxon t where t.authority_id = :authorityId";
	    stepExecution.getJobExecution().getExecutionContext().putLong("job.execution.id", stepExecution.getJobExecutionId());
	    queryString = queryString.replaceAll(":authorityId", authorityId.toString());
	    queryString = queryString.replaceAll(":jobId", stepExecution.getJobExecutionId().toString());
	    queryString = queryString.replaceAll(":dateTime", OlapDateTimeUserType.convert(new DateTime()).toString());
	    jdbcTemplate.update(queryString);
		return RepeatStatus.FINISHED;
	}
}
