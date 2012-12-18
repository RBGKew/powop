package org.emonocot.job.dwc.read;

import javax.sql.DataSource;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractRecordAnnotator implements StepExecutionListener {

	protected StepExecution stepExecution;

	protected JdbcTemplate jdbcTemplate;

	public final void setDataSource(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		jdbcTemplate.afterPropertiesSet();
	}

	public final void beforeStep(final StepExecution newStepExecution) {
		this.stepExecution = newStepExecution;
	}

	public final ExitStatus afterStep(final StepExecution newStepExecution) {
		return null;
	}

}
