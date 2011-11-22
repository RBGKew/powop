package org.emonocot.persistence.dao.olap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.emonocot.persistence.dao.JobExecutionDao;
import org.emonocot.persistence.dao.OlapResult;
import org.emonocot.persistence.olap.OlapExecutionException;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapParameterMetaData;
import org.olap4j.Position;
import org.olap4j.PreparedOlapStatement;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Member;
import org.olap4j.type.MemberType;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CleanupFailureDataAccessException;
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
     */
    private OlapConnection olapConnection;

    /**
     *
     * @param olapConnection Set the connection
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
    * @param pageNumber set the page number
    * @return a list of job executions
    */
    public final List<JobExecution> getJobExecutions(final String authorityName,
            final Integer pageSize, final Integer pageNumber) {
        if (pageSize == null || pageNumber == null) {
            return getJdbcTemplate().query("select bje.JOB_EXECUTION_ID, bje.START_TIME, bje.CREATE_TIME, bje.END_TIME, bje.STATUS, bje.EXIT_CODE, bje.EXIT_MESSAGE, bji.JOB_INSTANCE_ID, bji.JOB_NAME from BATCH_JOB_EXECUTION as bje join BATCH_JOB_PARAMS as bjp on (bje.JOB_INSTANCE_ID = bjp.JOB_INSTANCE_ID) join BATCH_JOB_INSTANCE as bji on (bje.JOB_INSTANCE_ID = bji.JOB_INSTANCE_ID) where bjp.KEY_NAME = 'authority.name' and bjp.STRING_VAL = ? order by START_TIME desc", rowMapper, authorityName);
        } else if (pageNumber == null) {
            return getJdbcTemplate().query("select bje.JOB_EXECUTION_ID, bje.START_TIME, bje.CREATE_TIME, bje.END_TIME, bje.STATUS, bje.EXIT_CODE, bje.EXIT_MESSAGE, bji.JOB_INSTANCE_ID, bji.JOB_NAME from BATCH_JOB_EXECUTION as bje join BATCH_JOB_PARAMS as bjp on (bje.JOB_INSTANCE_ID = bjp.JOB_INSTANCE_ID) join BATCH_JOB_INSTANCE as bji on (bje.JOB_INSTANCE_ID = bji.JOB_INSTANCE_ID) where bjp.KEY_NAME = 'authority.name' and bjp.STRING_VAL = ? order by START_TIME desc LIMIT ?", rowMapper, authorityName,pageSize);
        } else {
            return getJdbcTemplate().query("select bje.JOB_EXECUTION_ID, bje.START_TIME, bje.CREATE_TIME, bje.END_TIME, bje.STATUS, bje.EXIT_CODE, bje.EXIT_MESSAGE, bji.JOB_INSTANCE_ID, bji.JOB_NAME from BATCH_JOB_EXECUTION as bje join BATCH_JOB_PARAMS as bjp on (bje.JOB_INSTANCE_ID = bjp.JOB_INSTANCE_ID) join BATCH_JOB_INSTANCE as bji on (bje.JOB_INSTANCE_ID = bji.JOB_INSTANCE_ID) where bjp.KEY_NAME = 'authority.name' and bjp.STRING_VAL = ? order by START_TIME desc LIMIT ? OFFSET ?", rowMapper, authorityName,pageSize, pageNumber * pageSize);
        }
    }

    /**
    *
    * @param jobExecutionId Set the job execution identifier
    * @return a result object
    */
    public final List<OlapResult> countObjects(final Long jobExecutionId) {
        PreparedOlapStatement statement = null;
        try {
            String query = "SELECT {[Object Type].[Object Type].members} ON COLUMNS FROM Job  WHERE {[Job].[$1]}";
            query = query.replace("$1", jobExecutionId.toString());
            statement = olapConnection
                    .prepareOlapStatement(query);
            CellSet cellSet = statement.executeQuery();
            return resultList(cellSet);
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
     * @param cellSet set the cell set
     * @return a list of olap results
     */
    private List<OlapResult> resultList(final CellSet cellSet) {
        List<OlapResult> results = new ArrayList<OlapResult>();
        for (Position position : cellSet.getAxes().get(0).getPositions()) {
            results.add(new OlapResultImpl(cellSet.getCell(position)
                    .getFormattedValue(), position.getMembers().get(0)
                    .getCaption()));
        }
        return results;
    }

    /**
     *
     * @param statement the prepared statement
     * @param index Set the index
     * @param value Set the value
     * @throws SQLException if something goes wrong
     */
    private void setParameter(final PreparedOlapStatement statement,
            final int index, final String value) throws SQLException {
        OlapParameterMetaData parameterMetaData = statement
                .getParameterMetaData();
        MemberType type = (MemberType) parameterMetaData
                .getParameterOlapType(index);
        Dimension dimension = type.getDimension();
        Member allMembers = dimension.getDefaultHierarchy().getRootMembers()
                .get(0);
        statement.setObject(index, allMembers.getChildMembers().get(value));
    }

    /**
    *
    * @param jobExecutionId Set the job execution identifier
    * @param objectType set the object type
    * @return a result object
    */
    public final List<OlapResult> countErrors(final Long jobExecutionId,
            final String objectType) {
        PreparedOlapStatement statement = null;
        try {
            String query = "select {[Type].[Type].members} ON COLUMNS FROM Job WHERE ([Measures].[Annotation Numbers], [Job].[$1], [Object Type].[$2])";
            query = query.replace("$1", jobExecutionId.toString());
            query = query.replace("$2", objectType);
            statement =  olapConnection
                .prepareOlapStatement(query);
            CellSet cellSet = statement.executeQuery();
            return resultList(cellSet);
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
        JobExecution jobExecution = getJdbcTemplate().queryForObject("select bje.JOB_EXECUTION_ID, bje.START_TIME, bje.CREATE_TIME, bje.END_TIME, bje.STATUS, bje.EXIT_CODE, bje.EXIT_MESSAGE, bji.JOB_INSTANCE_ID, bji.JOB_NAME from BATCH_JOB_EXECUTION as bje join BATCH_JOB_INSTANCE as bji on (bje.JOB_INSTANCE_ID = bji.JOB_INSTANCE_ID) where bje.JOB_EXECUTION_ID = ?", rowMapper, identifier);
        return jobExecution;
    }

    /**
     *
     * @param id The id to delete
     */
    public final void delete(final long id) {
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
