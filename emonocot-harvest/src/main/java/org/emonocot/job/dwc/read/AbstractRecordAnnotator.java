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
package org.emonocot.job.dwc.read;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

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
