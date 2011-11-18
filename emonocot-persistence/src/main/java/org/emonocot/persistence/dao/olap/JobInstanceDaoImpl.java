package org.emonocot.persistence.dao.olap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
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

    /**
     *
     * @param id
     *            The id to delete
     */
    public final void delete(final long id) {
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
        getJdbcTemplate().update(
                "INSERT into BATCH_JOB_INSTANCE(JOB_INSTANCE_ID, JOB_NAME, VERSION)"
                        + " values (?, ?, ?)", jobInstance.getId(),
                jobInstance.getJobName(), jobInstance.getVersion());
        for (String key : jobInstance.getJobParameters().getParameters()
                .keySet()) {
            JobParameter jobParameter = jobInstance.getJobParameters()
                    .getParameters().get(key);
            insertParameter(jobInstance.getId(), jobParameter.getType(), key,
                    jobParameter.getValue());
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
            jobInstance.setVersion(resultSet.getBigDecimal("VERSION")
                    .intValue());
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
         + "STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL from %PREFIX%JOB_PARAMS where JOB_INSTANCE_ID = ?", rowCallbackHandler, instanceId);
        return new JobParameters(map);
    }
}
