package org.emonocot.persistence.dao.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import mondrian.olap.Connection;
import mondrian.olap.Query;
import mondrian.olap.Result;

import org.emonocot.persistence.dao.JobDao;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
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
public class JobDaoImpl extends JdbcDaoSupport implements JobDao {
    /**
     *
     */
    private RowMapper<JobExecution> rowMapper = new JobExecutionRowMapper();

    /**
     *
     */
    private Connection connection;

    /**
     *
     * @param connection Set the connection
     */
    @Autowired
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     *
     * @param dataSource Set the data source
     */
    @Autowired
    public void setDatasource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

   /**
    *
    * @param authorityName the name of the authorty
    * @param pageSize set the maximum size of the list of executions
    * @return a list of job executions
    */
    public final List<JobExecution> getJobExecutions(final String authorityName,
            final Integer pageSize) {
        if (pageSize != null) {
            getJdbcTemplate().setMaxRows(pageSize);
        } else {
            getJdbcTemplate().setMaxRows(10);
        }
        return getJdbcTemplate().query("select bje.JOB_EXECUTION_ID, bje.CREATE_TIME, bje.END_TIME, bje.STATUS, bje.EXIT_CODE, bje.EXIT_MESSAGE from BATCH_JOB_EXECUTION as bje join BATCH_JOB_PARAMS as bjp on (bje.JOB_INSTANCE_ID = bjp.JOB_INSTANCE_ID) where bjp.KEY_NAME = 'authority.name' and bjp.STRING_VAL = ? order by START_TIME desc", rowMapper, authorityName);
    }

    /**
    *
    * @param jobExecutionId Set the job execution identifier
    * @return a result object
    */
    public final Result countObjects(final Long jobExecutionId) {
        Query query = connection
                .parseQuery("select {[Object Type].[Object Type].members} on columns from Job  where Parameter(\"jobExecutionId\", [Job],[Job].[1])");
        query.setParameter("jobExecutionId", jobExecutionId.toString());
        return connection.execute(query);
    }

    /**
    *
    * @param jobExecutionId Set the job execution identifier
    * @param objectType set the object type
    * @return a result object
    */
    public final Result countErrors(final Long jobExecutionId,
            final String objectType) {
        Query query = connection
                .parseQuery("select {[Type].[Type].members} on columns from Job where (Parameter(\"jobExecutionId\", [Job],[Job].[1]), Parameter(\"objectType\",[Object Type],[Object Type].[Taxon]))");
        query.setParameter("jobExecutionId", jobExecutionId.toString());
        query.setParameter("objectType", objectType);
        return connection.execute(query);
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
            JobExecution jobExecution = new JobExecution(
                    resultSet.getLong("JOB_EXECUTION_ID"));
            jobExecution.setCreateTime(resultSet.getDate("CREATE_TIME"));
            jobExecution.setEndTime(resultSet.getDate("END_TIME"));
            jobExecution.setStatus(BatchStatus.valueOf(resultSet
                    .getString("STATUS")));
            ExitStatus exitStatus = new ExitStatus(
                    resultSet.getString("EXIT_CODE"),
                    resultSet.getString("EXIT_MESSAGE"));
            jobExecution.setExitStatus(exitStatus);
            return jobExecution;
        }

    }
}
