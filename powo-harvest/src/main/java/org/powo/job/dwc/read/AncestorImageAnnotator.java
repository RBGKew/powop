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

import java.util.List;
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
import com.google.common.collect.Lists;

public class AncestorImageAnnotator implements StepExecutionListener, Tasklet {

	private Logger logger = LoggerFactory.getLogger(RecordAnnotator.class);

	protected StepExecution stepExecution;

	protected NamedParameterJdbcTemplate jdbcTemplate;

	private String authorityName;

	private String resourceIdentifier;

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public void setResourceIdentifier(String resourceIdentifier) {
		this.resourceIdentifier = resourceIdentifier;
	}

	protected Long getAuthorityId() {
		String sql = "SELECT id FROM Organisation WHERE identifier = :authorityName";
		return jdbcTemplate.queryForObject(sql, ImmutableMap.of("authorityName", authorityName), Long.class);
	}

	protected Long getResourceId() {
		String sql = "Select id from Resource where identifier = :resourceIdentifier";
		return jdbcTemplate.queryForObject(sql, ImmutableMap.of("resourceIdentifier", resourceIdentifier), Long.class);
	}

	private List<Long> getAssociatedFamilyIds(Long resourceId) {
		String query = "SELECT t.id "
				+ "FROM taxon t "
				+ "WHERE t.taxonRank = 'FAMILY' AND t.family in "
				+ "(SELECT DISTINCT t.family "
					+ "FROM taxon t, image i "
					+ "WHERE t.id = i.taxon_id AND i.resource_id = :resourceId)";
		return jdbcTemplate.queryForList(query, ImmutableMap.<String, Long>of("resourceId", resourceId), Long.class);
	}

	private List<Long> getAssociatedGeneraIds(Long resourceId) {
		String query = "SELECT t.id "
				+ "FROM taxon t, "
				+ "(SELECT  t.family, t.genus "
					+ "FROM taxon t, image i "
					+ "WHERE t.id = i.taxon_id AND i.resource_id = :resourceId "
					+ "GROUP BY t.family, t.genus) a "
				+ "WHERE t.family = a.family AND t.genus = a.genus AND t.taxonRank = 'GENUS'";
		return jdbcTemplate.queryForList(query, ImmutableMap.<String, Long>of("resourceId", resourceId), Long.class);
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Long resourceId = getResourceId();
		Map<String, ? extends Object> queryParameters = ImmutableMap.<String, Object>of(
				"authorityId", getAuthorityId(),
				"resourceId", resourceId,
				"jobId", stepExecution.getJobExecutionId());

		// Annotate any parent taxa for re-indexing due to image 'bubble-up'
		String queryString = "INSERT INTO Annotation (annotatedObjId, annotatedObjType, jobId, dateTime, authority_id, resource_id, type, code, recordType) VALUES ";
		List<Long> relatedTaxa = getAssociatedFamilyIds(resourceId);
		relatedTaxa.addAll(getAssociatedGeneraIds(resourceId));

		for(List<Long> partition : Lists.partition(relatedTaxa, 1000)) {
			StringBuilder values = new StringBuilder();
			for(Long id : partition) {
				values.append(String.format("(%s, 'Taxon', :jobId, now(), :authorityId, :resourceId, 'Info', 'Index', 'Taxon'),", id));
			}

			if(values.length() > 0) {
				values.setCharAt(values.length()-1, ';');
				String q = queryString + values.toString();

				logger.debug("Annotating: {} with params {}", q, queryParameters);
				jdbcTemplate.update(q, queryParameters);
			}
		}

		return RepeatStatus.FINISHED;
	}

	public void beforeStep(StepExecution newStepExecution) {
		this.stepExecution = newStepExecution;
	}

	public ExitStatus afterStep(StepExecution newStepExecution) {
		return null;
	}
}
