package org.emonocot.checklist.logging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 *
 * @author ben
 *
 */
public class AltDBAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    /**
    *
    */
   private static final int SEARCH_TYPE_INDEX = 1;

    /**
     *
     */
    private static final int CHECKLIST_INDEX = 2;

    /**
     *
     */
    private static final int REQ_HOST_INDEX = 3;

   /**
    *
    */
    private static final int FAMILY_INDEX = 4;

    /**
     *
     */
    private static final int GENUS_INDEX = 5;

    /**
     *
     */
    private static final int IP_ADDRESS_INDEX = 6;

    /**
     *
     */
    private static final int RESULT_COUNT_INDEX = 7;

   /**
    *
    */
    private static final int SEARCH_DATE_INDEX = 8;

    /**
     *
     */
    private static final int SEARCH_TYPE_LENGTH = 50;

    /**
     *
     */
    private static final int FAMILY_LENGTH = 30;

    /**
     *
     */
    private static final int GENUS_LENGTH = 30;

    /**
     *
     */
    private static final int IP_ADDRESS_LENGTH = 15;



    /**
     *
     */
    private static String INSERT_SQL = null;

    static {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        sqlBuilder.append("web_log").append(" (");
        sqlBuilder.append("search_type").append(", ");
        sqlBuilder.append("checklist").append(", ");
        sqlBuilder.append("req_host").append(", ");
        sqlBuilder.append("family").append(", ");
        sqlBuilder.append("genus").append(", ");
        sqlBuilder.append("ip_address").append(", ");
        sqlBuilder.append("result_count").append(", ");
        sqlBuilder.append("search_date").append(") ");
        sqlBuilder.append("VALUES (?, ?, ? , ?, ?, ?, ?, ?)");
        INSERT_SQL =  sqlBuilder.toString();
      }

    /**
     *
     */
    private JdbcTemplate jdbcTemplate;


    /**
     * @return the dataSource
     */
    public final DataSource getDataSource() {
        return jdbcTemplate.getDataSource();
    }


    /**
     * @param dataSource the dataSource to set
     */
    public final void setDataSource(final DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalStateException(
                "DBAppender cannot function without a data source");
        }
        this.jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
    }

    /**
     * @param loggingEvent the event to log
     */
    @Override
    protected final void append(final ILoggingEvent loggingEvent) {
        Logger logger = LoggerFactory.getLogger(AltDBAppender.class);
        try {
        jdbcTemplate.update(new PreparedStatementCreator() {

            /**
             *
             */
            public PreparedStatement
            createPreparedStatement(final Connection connection)
                    throws SQLException {
                PreparedStatement preparedStatement =
                    connection.prepareStatement(INSERT_SQL);
                preparedStatement.setTimestamp(SEARCH_DATE_INDEX,
                        new Timestamp(loggingEvent.getTimeStamp()));
                preparedStatement.setString(CHECKLIST_INDEX,
                        "selected_families");
                Map<String, String> mergedMap = mergePropertyMaps(loggingEvent);
                bindParam(mergedMap, SEARCH_TYPE_INDEX,
                        LoggingConstants.SEARCH_TYPE_KEY, preparedStatement, 2);
                bindParam(mergedMap, REQ_HOST_INDEX,
                        LoggingConstants.MDC_CLIENT_NAME_KEY, preparedStatement,
                        SEARCH_TYPE_LENGTH);
                bindParam(mergedMap, FAMILY_INDEX,
                        LoggingConstants.FAMILY_KEY, preparedStatement,
                        FAMILY_LENGTH);
                bindParam(mergedMap, GENUS_INDEX,
                        LoggingConstants.QUERY_KEY, preparedStatement,
                        GENUS_LENGTH);
                bindParam(mergedMap, IP_ADDRESS_INDEX,
                        ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY,
                        preparedStatement, IP_ADDRESS_LENGTH);
                bindIntParam(mergedMap, RESULT_COUNT_INDEX,
                        LoggingConstants.RESULT_COUNT_KEY, preparedStatement);
                return preparedStatement;
            } });
        } catch (DataAccessException dae) {
            logger.debug(dae.getMessage());
            throw dae;
        }
        logger.info("Logged event successfully");
    }

    /**
    *
    * @param map the map of parameters
    * @param index Set the index of the parameter of interest
    * @param key Set the string of the parameter
    * @param stmt Set the prepared statement
    * @param size the size of the field
    * @throws SQLException if something goes wrong
    */
   private void bindParam(final Map<String, String> map,
           final int index, final String key,
           final PreparedStatement stmt, final int size) throws SQLException {
       if (map.containsKey(key)) {
           String value = map.get(key);
           if (value.length() > size) {
               value = value.substring(0, size);
           }
           stmt.setString(index, value);
         } else {
             stmt.setString(index, "");
         }
   }

  /**
   *
   * @param map the map of parameters
   * @param index Set the index of the parameter of interest
   * @param key Set the string of the parameter
   * @param stmt Set the prepared statement
   * @throws SQLException if something goes wrong
   */
  private void bindIntParam(final Map<String, String> map,
          final int index, final String key,
          final PreparedStatement stmt) throws SQLException {
      if (map.containsKey(key)) {
          stmt.setInt(index, Integer.parseInt(map.get(key)));
        } else {
            stmt.setInt(index, 0);
        }
  }

   /**
    *
    * @param event Set the logging event
    * @return a merged map of properties
    */
   final Map<String, String> mergePropertyMaps(final ILoggingEvent event) {
       Map<String, String> mergedMap = new HashMap<String, String>();
       // we add the context properties first, then the event properties, since
       // we consider that event-specific properties should have priority over
       // context-wide properties.
       Map<String, String> loggerContextMap = event.getLoggerContextVO()
           .getPropertyMap();
       Map<String, String> mdcMap = event.getMDCPropertyMap();
       if (loggerContextMap != null) {
         mergedMap.putAll(loggerContextMap);
       }
       if (mdcMap != null) {
         mergedMap.putAll(mdcMap);
       }

       return mergedMap;
     }
}
