package org.emonocot.job.dwc;

import javax.sql.DataSource;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractRecordAnnotator implements StepExecutionListener {

	/**
	 *
	 */
	protected StepExecution stepExecution;

	/**
	 *
	 */
	protected JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * @param sessionFactory
	 *            Set the session factory
	 */
	public final void setDataSource(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		jdbcTemplate.afterPropertiesSet();
	}

	/**
	 * @param newStepExecution
	 *            Set the step execution
	 */
	public final void beforeStep(final StepExecution newStepExecution) {
		this.stepExecution = newStepExecution;
	}

	/**
	 * @param newStepExecution
	 *            Set the step execution
	 * @return the exit status
	 */
	public final ExitStatus afterStep(final StepExecution newStepExecution) {
		return null;
	}

}