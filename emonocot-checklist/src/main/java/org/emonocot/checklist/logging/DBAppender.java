package org.emonocot.checklist.logging;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.db.names.DBNameResolver;
import ch.qos.logback.classic.db.names.DefaultDBNameResolver;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.db.DBAppenderBase;

/**
 *
 * @author ben
 *
 */
public class DBAppender extends DBAppenderBase<ILoggingEvent> {

    /**
     *
     */
    protected static final Method GET_GENERATED_KEYS_METHOD;

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
    private String insertSQL;

    /**
     *
     */
    private DBNameResolver dbNameResolver;

    static {
        // PreparedStatement.getGeneratedKeys() method was added in JDK 1.4
        Method getGeneratedKeysMethod;
        try {
            // the
            getGeneratedKeysMethod = PreparedStatement.class.getMethod(
                    "getGeneratedKeys", (Class[]) null);
        } catch (Exception ex) {
            getGeneratedKeysMethod = null;
        }
        GET_GENERATED_KEYS_METHOD = getGeneratedKeysMethod;
    }

    /**
     *
     * @param dbNameResolver the database name resolver
     * @return the sql string
     */
    static String buildInsertSQL(final DBNameResolver dbNameResolver) {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        sqlBuilder.append(dbNameResolver.getTableName(TableName.web_log)).append(" (");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.search_type)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.checklist)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.req_host)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.family)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.genus)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.ip_address)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.result_count)).append(", ");
        sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.search_date)).append(") ");
        sqlBuilder.append("VALUES (?, ?, ? , ?, ?, ?, ?, ?)");
        return sqlBuilder.toString();
      }

    /**
     *
     */
    @Override
    public final void start() {
      if (dbNameResolver == null) {
        dbNameResolver = new DefaultDBNameResolver();
      }

      insertSQL = buildInsertSQL(dbNameResolver);
      super.start();
    }

    /**
     *
     * @param stmt the prepared statement
     * @param event the logging event
     * @throws SQLException if there is a problem binding this statement
     */
    final void bindLoggingEventWithInsertStatement(final PreparedStatement stmt,
            final ILoggingEvent event) throws SQLException {
          stmt.setTimestamp(SEARCH_DATE_INDEX,
                  new Timestamp(event.getTimeStamp()));
          stmt.setString(CHECKLIST_INDEX, "selected_families");
          Map<String, String> mergedMap = mergePropertyMaps(event);
          bindParam(mergedMap, SEARCH_TYPE_INDEX,
                  LoggingConstants.SEARCH_TYPE_KEY, stmt, 2);
          bindParam(mergedMap, REQ_HOST_INDEX,
                  LoggingConstants.MDC_CLIENT_NAME_KEY, stmt,
                  SEARCH_TYPE_LENGTH);
          bindParam(mergedMap, FAMILY_INDEX,
                  LoggingConstants.FAMILY_KEY, stmt, FAMILY_LENGTH);
          bindParam(mergedMap, GENUS_INDEX,
                  LoggingConstants.QUERY_KEY, stmt, GENUS_LENGTH);
          bindParam(mergedMap, IP_ADDRESS_INDEX,
                  ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY, stmt,
                  IP_ADDRESS_LENGTH);
          bindIntParam(mergedMap, RESULT_COUNT_INDEX,
                  LoggingConstants.RESULT_COUNT_KEY, stmt);
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

    /**
     * @return a method
     */
    @Override
    protected final Method getGeneratedKeysMethod() {
        return GET_GENERATED_KEYS_METHOD;
    }

    /**
     * @return the statement to insert
     */
    @Override
    protected final String getInsertSQL() {
        return insertSQL;
    }

    /**
     * @param event Set the logging event
     * @param connection Set the database connection
     * @param eventId Set the event identifier
     * @throws Throwable if there is a problem with this method
     */
    @Override
    protected final void secondarySubAppend(final ILoggingEvent event,
            final Connection connection, final long eventId) throws Throwable {
        // do nothing
    }

    /**
     * @param event Set the logging event
     * @param connection the database connection
     * @param insertStatement the insert statement
     * @throws Throwable if there is a problem with this method
     */
    @Override
    protected final void subAppend(final ILoggingEvent event,
            final Connection connection, final PreparedStatement insertStatement)
            throws Throwable {

        bindLoggingEventWithInsertStatement(insertStatement, event);
        int updateCount = insertStatement.executeUpdate();
        if (updateCount != 1) {
            addWarn("Failed to insert loggingEvent");
        }

    }
}
