package org.emonocot.persistence.dao.olap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.OlapParameterMetaData;
import org.olap4j.PreparedOlapStatement;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Member;
import org.olap4j.type.MemberType;

import org.emonocot.persistence.dao.JobDao;
import org.emonocot.persistence.olap.OlapExecutionException;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CleanupFailureDataAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.UncategorizedDataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
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
    private OlapConnection olapConnection;

    /**
     *
     * @param connection Set the connection
     */
    @Autowired
    public void setOlapConnection(OlapConnection olapConnection) {
        this.olapConnection = olapConnection;
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
    public final CellSet countObjects(final Long jobExecutionId) {
        PreparedOlapStatement statement = null;
        try {
            statement = olapConnection
                    .prepareOlapStatement("select {[Object Type].[Object Type].members} on columns from Job  where Parameter(\"jobExecutionId\", [Job],[Job].[1])");
            setParameter(statement, 1, jobExecutionId.toString());
            return statement.executeQuery();
        } catch (Exception e) {
            throw new OlapExecutionException("OlapException", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw new CleanupFailureDataAccessException(
                            "Could not Clean up", sqle);
                }
            }
        }
    }

    /**
     *
     * @param statement the prepared statement
     * @param index Set the index
     * @param value Set the value
     * @throws SQLException if something goes wrong
     */
    private void setParameter(PreparedOlapStatement statement,
            int index, String value) throws SQLException {
        OlapParameterMetaData parameterMetaData = statement.getParameterMetaData();
        MemberType type = (MemberType) parameterMetaData.getParameterOlapType(index);  
        Dimension dimension = type.getDimension();
        statement.setObject(index, dimension.getDefaultHierarchy().getRootMembers().get(value));
    }

    /**
    *
    * @param jobExecutionId Set the job execution identifier
    * @param objectType set the object type
    * @return a result object
    */
    public final CellSet countErrors(final Long jobExecutionId,
            final String objectType) {
        PreparedOlapStatement statement = null;
        try {
          statement =  olapConnection
                .prepareOlapStatement("select {[Type].[Type].members} on columns from Job where (Parameter(\"jobExecutionId\", [Job],[Job].[1]), Parameter(\"objectType\",[Object Type],[Object Type].[Taxon]))");
          setParameter(statement, 1, jobExecutionId.toString());
          setParameter(statement, 2, objectType);
          return statement.executeQuery();
        } catch(Exception e) {
            throw new OlapExecutionException("OlapException",e);
        } finally {
            if (statement != null) {
                try {
                statement.close();
                } catch(SQLException sqle) {
                    throw new CleanupFailureDataAccessException("Could not Clean up", sqle);
                }
            }
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
