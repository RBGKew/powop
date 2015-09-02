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
package org.emonocot.persistence.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.emonocot.persistence.dao.JobExecutionDao;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class JobExecutionDaoImpl extends JdbcDaoSupport implements
JobExecutionDao {
	/**
	 *
	 */
	private RowMapper<JobExecution> rowMapper = new JobExecutionRowMapper();

	/**
	 *
	 * @param dataSource Set the data source
	 */
	@Autowired
	public final void setDatasource(final DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	/**
	 *
	 * @param authorityName the name of the authorty
	 * @param pageSize set the maximum size of the list of executions
	 * @param pageNumber set the page number
	 * @return a list of job executions
	 */
	public final List<JobExecution> getJobExecutions(final String authorityName,
			final Integer pageSize, final Integer pageNumber) {
		if (pageSize == null && pageNumber == null) {
			if(authorityName == null) {
				return getJdbcTemplate().query("select bje.JOB_EXECUTION_ID as JOB_EXECUTION_ID, bje.START_TIME as START_TIME, bje.CREATE_TIME as CREATE_TIME, bje.END_TIME as END_TIME, bje.STATUS as STATUS, bje.EXIT_CODE as EXIT_CODE, bje.EXIT_MESSAGE as EXIT_MESSAGE, bji.JOB_INSTANCE_ID as JOB_INSTANCE_ID, bji.JOB_NAME as JOB_NAME from BATCH_JOB_EXECUTION as bje join BATCH_JOB_PARAMS as bjp on (bje.JOB_INSTANCE_ID = bjp.JOB_INSTANCE_ID) join BATCH_JOB_INSTANCE as bji on (bje.JOB_INSTANCE_ID = bji.JOB_INSTANCE_ID) order by START_TIME desc", rowMapper);
			} else {
				return getJdbcTemplate().query("select bje.JOB_EXECUTION_ID as JOB_EXECUTION_ID, bje.START_TIME as START_TIME, bje.CREATE_TIME as CREATE_TIME, bje.END_TIME as END_TIME, bje.STATUS as STATUS, bje.EXIT_CODE as EXIT_CODE, bje.EXIT_MESSAGE as EXIT_MESSAGE, bji.JOB_INSTANCE_ID as JOB_INSTANCE_ID, bji.JOB_NAME as JOB_NAME from BATCH_JOB_EXECUTION as bje join BATCH_JOB_PARAMS as bjp on (bje.JOB_INSTANCE_ID = bjp.JOB_INSTANCE_ID) join BATCH_JOB_INSTANCE as bji on (bje.JOB_INSTANCE_ID = bji.JOB_INSTANCE_ID) where bjp.KEY_NAME = 'authority.name' and bjp.STRING_VAL = ? order by START_TIME desc", rowMapper, authorityName);
			}
		} else if (pageNumber == null) {
			return getJdbcTemplate().query("select bje.JOB_EXECUTION_ID as JOB_EXECUTION_ID, bje.START_TIME as START_TIME, bje.CREATE_TIME as CREATE_TIME, bje.END_TIME as END_TIME, bje.STATUS as STATUS, bje.EXIT_CODE as EXIT_CODE, bje.EXIT_MESSAGE as EXIT_MESSAGE, bji.JOB_INSTANCE_ID as JOB_INSTANCE_ID, bji.JOB_NAME as JOB_NAME from BATCH_JOB_EXECUTION as bje join BATCH_JOB_PARAMS as bjp on (bje.JOB_INSTANCE_ID = bjp.JOB_INSTANCE_ID) join BATCH_JOB_INSTANCE as bji on (bje.JOB_INSTANCE_ID = bji.JOB_INSTANCE_ID) where bjp.KEY_NAME = 'authority.name' and bjp.STRING_VAL = ? order by START_TIME desc LIMIT ?", rowMapper, authorityName,pageSize);
		} else {
			return getJdbcTemplate().query("select bje.JOB_EXECUTION_ID as JOB_EXECUTION_ID, bje.START_TIME as START_TIME, bje.CREATE_TIME as CREATE_TIME, bje.END_TIME as END_TIME, bje.STATUS as STATUS, bje.EXIT_CODE as EXIT_CODE, bje.EXIT_MESSAGE as EXIT_MESSAGE, bji.JOB_INSTANCE_ID as JOB_INSTANCE_ID, bji.JOB_NAME as JOB_NAME from BATCH_JOB_EXECUTION as bje join BATCH_JOB_PARAMS as bjp on (bje.JOB_INSTANCE_ID = bjp.JOB_INSTANCE_ID) join BATCH_JOB_INSTANCE as bji on (bje.JOB_INSTANCE_ID = bji.JOB_INSTANCE_ID) where bjp.KEY_NAME = 'authority.name' and bjp.STRING_VAL = ? order by START_TIME desc LIMIT ? OFFSET ?", rowMapper, authorityName,pageSize, pageNumber * pageSize);
		}
	}

	/**
	 *
	 * @author ben
	 *
	 */
	public class JobExecutionRowMapper implements RowMapper<JobExecution> {

		/**
		 * @param resultSet Set the result set
		 * @param rowNumber Set the row number
		 * @throws SQLException if there is a problem
		 * @return a job execution instance
		 */
		public final JobExecution mapRow(final ResultSet resultSet,
				final int rowNumber) throws SQLException {
			JobInstance jobInstance = new JobInstance(resultSet.getBigDecimal(
					"JOB_INSTANCE_ID").longValue(),
					new JobParameters(), resultSet.getString("JOB_NAME"));
			JobExecution jobExecution = new JobExecution(jobInstance,
					resultSet.getBigDecimal("JOB_EXECUTION_ID").longValue());
			jobExecution.setStartTime(resultSet.getTimestamp("START_TIME"));
			jobExecution.setCreateTime(resultSet.getTimestamp("CREATE_TIME"));
			jobExecution.setEndTime(resultSet.getTimestamp("END_TIME"));
			jobExecution.setStatus(BatchStatus.valueOf(resultSet
					.getString("STATUS")));
			ExitStatus exitStatus = new ExitStatus(
					resultSet.getString("EXIT_CODE"),
					resultSet.getString("EXIT_MESSAGE"));
			jobExecution.setExitStatus(exitStatus);
			return jobExecution;
		}

	}

	/**
	 *
	 * @param identifier the identifier of the job
	 * @return a job execution
	 */
	public final JobExecution load(final Long identifier) {
		JobExecution jobExecution = getJdbcTemplate().queryForObject("select bje.JOB_EXECUTION_ID as JOB_EXECUTION_ID, bje.START_TIME as START_TIME, bje.CREATE_TIME as CREATE_TIME, bje.END_TIME as END_TIME, bje.STATUS as STATUS, bje.EXIT_CODE as EXIT_CODE, bje.EXIT_MESSAGE as EXIT_MESSAGE, bji.JOB_INSTANCE_ID as JOB_INSTANCE_ID, bji.JOB_NAME as JOB_NAME from BATCH_JOB_EXECUTION as bje join BATCH_JOB_INSTANCE as bji on (bje.JOB_INSTANCE_ID = bji.JOB_INSTANCE_ID) where bje.JOB_EXECUTION_ID = ?", rowMapper, identifier);
		return jobExecution;
	}

	/**
	 *
	 * @param id The id to delete
	 */
	public final void delete(final Long id) {
		getJdbcTemplate().update("DELETE from BATCH_JOB_EXECUTION where JOB_EXECUTION_ID = ?", id);
	}

	/**
	 *
	 * @param jobExecution The jobExecution to save
	 */
	public final void save(final JobExecution jobExecution) {
		String exitCode = null;
		String exitDescription = null;
		if (jobExecution.getExitStatus() != null) {
			exitCode = jobExecution.getExitStatus().getExitCode();
			exitDescription = jobExecution.getExitStatus().getExitDescription();
		}
		getJdbcTemplate()
		.update("INSERT INTO BATCH_JOB_EXECUTION (JOB_EXECUTION_ID, VERSION, JOB_INSTANCE_ID, CREATE_TIME, START_TIME, END_TIME, STATUS, EXIT_CODE, EXIT_MESSAGE, LAST_UPDATED) VALUES (?,?,?,?,?,?,?,?,?,?)",
				jobExecution.getId(),
				jobExecution.getVersion(),
				jobExecution.getJobInstance().getId(),
				jobExecution.getCreateTime(),
				jobExecution.getStartTime(),
				jobExecution.getEndTime(),
				jobExecution.getStatus().name(),
				exitCode,
				exitDescription,
				jobExecution.getLastUpdated()
				);
	}
}

