package org.emonocot.job.dwc.read;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public abstract class AbstractRecordAnnotator implements StepExecutionListener {
	
	private Logger logger = LoggerFactory.getLogger(AbstractRecordAnnotator.class);

	protected StepExecution stepExecution;

	protected NamedParameterJdbcTemplate jdbcTemplate;
	
    private String authorityName;
    
	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	protected Integer annotate(String annotationQuery, Map<String,Object> annotationParameters) {
		Integer authorityId = getAuthorityId();
	    stepExecution.getJobExecution().getExecutionContext().putLong("job.execution.id", stepExecution.getJobExecutionId());
		
		annotationParameters.put("authorityId", authorityId);
		annotationParameters.put("jobId", stepExecution.getJobExecutionId());
		annotationParameters.put("dateTime", new DateTime().toDate());   

	    logger.info(annotationQuery);
	    return jdbcTemplate.update(annotationQuery, annotationParameters);
	}
	
	protected Integer getAuthorityId() {
		String authorityQuerySQL = "Select id from Organisation where identifier = :authorityName";
		Map<String,Object> authorityQueryParameters = new HashMap<String,Object>();
		authorityQueryParameters.put("authorityName", authorityName);
		return jdbcTemplate.queryForInt(authorityQuerySQL,authorityQueryParameters);
	}

	public void beforeStep(StepExecution newStepExecution) {
		this.stepExecution = newStepExecution;
	}

	public ExitStatus afterStep(StepExecution newStepExecution) {
		return null;
	}

}
