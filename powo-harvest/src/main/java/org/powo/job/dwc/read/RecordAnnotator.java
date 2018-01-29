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
package org.powo.job.dwc.read;

import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.google.common.collect.ImmutableMap;

public class RecordAnnotator implements StepExecutionListener, Tasklet {

	private Logger logger = LoggerFactory.getLogger(RecordAnnotator.class);

	protected StepExecution stepExecution;

	protected NamedParameterJdbcTemplate jdbcTemplate;

	private String authorityName;
	private Long resourceId;
	private String annotatedObjType;

	public void setAnnotatedObjectType(String objectType) {
		this.annotatedObjType = objectType;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	protected Long getAuthorityId() {
		String authorityQuerySQL = "Select id from Organisation where identifier = :authorityName";
		return jdbcTemplate.queryForObject(authorityQuerySQL, ImmutableMap.of("authorityName", authorityName), Long.class);
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		String queryString = "INSERT INTO Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, resource_id, type, code, recordType) "
				+ "SELECT o.id, :annotatedObjType, :jobId, now(), :authorityId, :resourceId, 'Warn', 'Absent', :annotatedObjType "
				+ "FROM " + annotatedObjType + " o "
				+ "WHERE o.authority_id = :authorityId AND o.resource_id = :resourceId";
		stepExecution.getJobExecution().getExecutionContext().putLong("job.execution.id", stepExecution.getJobExecutionId());

		Map<String, ? extends Object> queryParameters = ImmutableMap.of(
				"authorityId", getAuthorityId(),
				"resourceId", resourceId,
				"jobId", stepExecution.getJobExecutionId(),
				"annotatedObjType", annotatedObjType);

		logger.debug("Annotating: {} with params {}", queryString, queryParameters);
		jdbcTemplate.update(queryString, queryParameters);

		return RepeatStatus.FINISHED;
	}

	public void beforeStep(StepExecution newStepExecution) {
		this.stepExecution = newStepExecution;
	}

	public ExitStatus afterStep(StepExecution newStepExecution) {
		return null;
	}
}
