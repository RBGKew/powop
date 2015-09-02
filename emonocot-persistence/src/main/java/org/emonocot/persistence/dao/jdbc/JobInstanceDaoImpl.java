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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.emonocot.persistence.dao.JobInstanceDao;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameter.ParameterType;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class JobInstanceDaoImpl extends JdbcDaoSupport implements
JobInstanceDao {

	/**
	 *
	 * @param dataSource
	 *            Set the data source
	 */
	@Autowired
	public void setDatasource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	/**
	 *
	 * @param identifier
	 *            the identifier of the job
	 * @return a job execution
	 */
	public final JobInstance load(final Long identifier) {
		JobParameters jobParameters = getJobParameters(identifier);
		RowMapper<JobInstance> rowMapper = new JobInstanceRowMapper(jobParameters);
		JobInstance jobInstance = getJdbcTemplate()
				.queryForObject(
						"SELECT JOB_INSTANCE_ID, JOB_NAME, VERSION from BATCH_JOB_INSTANCE where JOB_INSTANCE_ID = ?",
						rowMapper, identifier);
		return jobInstance;
	}

	@Override
	public List<JobInstance> list(Integer page, Integer size) {
		RowMapper<JobInstance> rowMapper = new JobInstanceRowMapper();
		if (size == null && page == null) {
			return getJdbcTemplate().query("SELECT JOB_INSTANCE_ID, JOB_NAME, VERSION from BATCH_JOB_INSTANCE", rowMapper);
		} else if (page == null) {
			return getJdbcTemplate().query("SELECT JOB_INSTANCE_ID, JOB_NAME, VERSION from BATCH_JOB_INSTANCE LIMIT ?", rowMapper,size);
		} else {
			return getJdbcTemplate().query("SELECT JOB_INSTANCE_ID, JOB_NAME, VERSION from BATCH_JOB_INSTANCE LIMIT ? OFFSET ?", rowMapper,size, page * size);
		}
	}

	/**
	 *
	 * @param id
	 *            The id to delete
	 */
	public final void delete(final Long id) {
		getJdbcTemplate().update(
				"DELETE from BATCH_JOB_PARAMS where JOB_INSTANCE_ID = ?", id);
		getJdbcTemplate().update(
				"DELETE from BATCH_JOB_INSTANCE where JOB_INSTANCE_ID = ?", id);
	}

	/**
	 *
	 * @param jobInstance
	 *            The jobExecution to save
	 */
	public final void save(final JobInstance jobInstance) {
		String jobKey = createJobKey(jobInstance.getJobParameters());
		getJdbcTemplate().update(
				"INSERT into BATCH_JOB_INSTANCE(JOB_INSTANCE_ID, JOB_NAME, VERSION, JOB_KEY)"
						+ " values (?, ?, ?, ?)", jobInstance.getId(),
						jobInstance.getJobName(), jobInstance.getVersion(), jobKey);
		for (String key : jobInstance.getJobParameters().getParameters()
				.keySet()) {
			JobParameter jobParameter = jobInstance.getJobParameters()
					.getParameters().get(key);
			insertParameter(jobInstance.getId(), jobParameter.getType(), key,
					jobParameter.getValue());
		}
	}

	/**
	 *
	 * @param jobParameters Set the job parameters
	 * @return the generated job key
	 */
	private String createJobKey(final JobParameters jobParameters) {

		Map<String, JobParameter> props = jobParameters.getParameters();
		StringBuffer stringBuffer = new StringBuffer();
		List<String> keys = new ArrayList<String>(props.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			stringBuffer.append(key + "=" + props.get(key).toString()
					+ ";");
		}

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"MD5 algorithm not available.  Fatal (should be in the JDK).");
		}

		try {
			byte[] bytes = digest.digest(stringBuffer.toString()
					.getBytes("UTF-8"));
			return String.format("%032x", new BigInteger(1, bytes));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(
					"UTF-8 encoding not available.  Fatal (should be in the JDK).");
		}
	}

	/**
	 * Convenience method that inserts an individual records into the
	 * JobParameters table.
	 * @param jobId Set the job id
	 * @param type Set the parameter type
	 * @param key Set the parameter name
	 * @param value Set the parameter value
	 */
	private void insertParameter(final Long jobId, final ParameterType type,
			final String key, final Object value) {

		Object[] args = new Object[0];
		int[] argTypes = new int[] {Types.BIGINT, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.BIGINT,
				Types.DOUBLE };

		if (type == ParameterType.STRING) {
			args = new Object[] {jobId, key, type, value, new Timestamp(0L),
					0L, 0D };
		} else if (type == ParameterType.LONG) {
			args = new Object[] {jobId, key, type, "", new Timestamp(0L),
					value, new Double(0) };
		} else if (type == ParameterType.DOUBLE) {
			args = new Object[] {jobId, key, type, "", new Timestamp(0L), 0L,
					value };
		} else if (type == ParameterType.DATE) {
			args = new Object[] {jobId, key, type, "", value, 0L, 0D };
		}

		getJdbcTemplate().update("INSERT into BATCH_JOB_PARAMS(JOB_INSTANCE_ID, KEY_NAME, TYPE_CD, "
				+ "STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL) values (?, ?, ?, ?, ?, ?, ?)", args, argTypes);
	}

	/**
	 *
	 * @author ben
	 *
	 */
	public class JobInstanceRowMapper implements RowMapper<JobInstance> {

		/**
		 *
		 */
		private JobParameters jobParameters = new JobParameters();

		/**
		 *
		 * @param newJobParameters Set the job parameters
		 */
		public JobInstanceRowMapper(final JobParameters newJobParameters) {
			this.jobParameters = newJobParameters;
		}

		public JobInstanceRowMapper() {
		}

		/**
		 * @param resultSet
		 *            Set the result set
		 * @param rowNumber
		 *            Set the row number
		 * @throws SQLException
		 *             if there is a problem
		 * @return a job execution instance
		 */
		public final JobInstance mapRow(final ResultSet resultSet,
				final int rowNumber) throws SQLException {
			JobInstance jobInstance = new JobInstance(resultSet.getBigDecimal(
					"JOB_INSTANCE_ID").longValue(), jobParameters,
					resultSet.getString("JOB_NAME"));
			BigDecimal version = resultSet.getBigDecimal("VERSION");
			if (version != null) {
				jobInstance.setVersion(version.intValue());
			}
			return jobInstance;
		}

	}

	/**
	 * @param instanceId Set the Job instance id
	 * @return the job parameters for that job instance
	 */
	private JobParameters getJobParameters(final Long instanceId) {
		final Map<String, JobParameter> map
		= new HashMap<String, JobParameter>();
		RowCallbackHandler rowCallbackHandler = new RowCallbackHandler() {
			public void processRow(final ResultSet rs) throws SQLException {
				ParameterType type = ParameterType.valueOf(rs.getString(3));
				JobParameter value = null;
				if (type == ParameterType.STRING) {
					value = new JobParameter(rs.getString(4));
				} else if (type == ParameterType.LONG) {
					value = new JobParameter(rs.getLong(6));
				} else if (type == ParameterType.DOUBLE) {
					value = new JobParameter(rs.getDouble(7));
				} else if (type == ParameterType.DATE) {
					value = new JobParameter(rs.getTimestamp(5));
				}
				// No need to assert that value is not null because it's an enum
				map.put(rs.getString(2), value);
			}
		};
		getJdbcTemplate().query("SELECT JOB_INSTANCE_ID, KEY_NAME, TYPE_CD, "
				+ "STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL from BATCH_JOB_PARAMS where JOB_INSTANCE_ID = ?", rowCallbackHandler, instanceId);
		return new JobParameters(map);
	}
}

