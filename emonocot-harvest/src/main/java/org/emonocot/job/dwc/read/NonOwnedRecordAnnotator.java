package org.emonocot.job.dwc.read;

import org.emonocot.model.hibernate.OlapDateTimeUserType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;


public class NonOwnedRecordAnnotator extends AbstractRecordAnnotator implements Tasklet {
	private Logger logger = LoggerFactory.getLogger(NonOwnedRecordAnnotator.class);
	
    private String authorityName;

	private String annotatedObjType;

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}
	
	public void setAnnotatedObjType(String annotatedObjType) {
		this.annotatedObjType = annotatedObjType;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Integer authorityId = jdbcTemplate.queryForInt("Select id from Organisation where identifier = '" + authorityName + "'");
	    stepExecution.getJobExecution().getExecutionContext().putLong("job.execution.id", stepExecution.getJobExecutionId());
	    String queryString = "insert into Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, type, code, recordType) select o.id, ':annotatedObjType', ':jobId', :dateTime, :authorityId, 'Warn', 'Absent', ':annotatedObjType' from :annotatedObjType o where o.authority_id = :authorityId";
	    queryString = queryString.replaceAll(":authorityId", authorityId.toString());
	    queryString = queryString.replaceAll(":jobId", stepExecution.getJobExecutionId().toString());
	    queryString = queryString.replaceAll(":dateTime", OlapDateTimeUserType.convert(new DateTime()).toString());
	    queryString = queryString.replaceAll(":annotatedObjType", annotatedObjType);
	    logger.info(queryString);
	    jdbcTemplate.update(queryString);
		return RepeatStatus.FINISHED;
	}
}
