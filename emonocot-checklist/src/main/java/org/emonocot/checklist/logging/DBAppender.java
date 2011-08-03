package org.emonocot.checklist.logging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

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
public class DBAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    /**
    *
    */
    private static final int SEARCH_TYPE_INDEX = 1;

    /**
    *
    */
    private static final int IP_ADDRESS_INDEX = 2;

    /**
     *
     */
    private static final int CHECKLIST_INDEX = 3;

    /**
     *
     */
    private static final int REQ_REFERRER_INDEX = 4;

    /**
     *
     */
    private static final int REQ_HOST_INDEX = 5;

    /**
     *
     */
    private static final int REMOTE_HOST_INDEX = 6;

    /**
    *
    */
    private static final int FAMILY_INDEX = 7;

    /**
     *
     */
    private static final int GENUS_INDEX = 8;

    /**
     *
     */
    private static final int SPECIES_INDEX = 9;

    /**
     *
     */
    private static final int AUTHOR_INDEX = 10;

    /**
     *
     */
    private static final int INFRA_RANK_INDEX = 11;

    /**
     *
     */
    private static final int INFRA_EPITHET_INDEX = 12;

    /**
     *
     */
    private static final int PUBLICATION_INDEX = 13;

    /**
     *
     */
    private static final int PUBLISHED_INDEX = 14;

    /**
     *
     */
    private static final int TDWG_CODES_INDEX = 15;

    /**
     *
     */
    private static final int RESULT_COUNT_INDEX = 16;

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
    private static final int SPECIES_LENGTH = 50;

    /**
     *
     */
    private String insertSQL = null;

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
     * @param dataSource
     *            the dataSource to set
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
     * @param loggingEvent
     *            the event to log
     */
    @Override
    protected final void append(final ILoggingEvent loggingEvent) {
        try {
            final String family;
            final String genus;
            final String specificEpithet;

            if (loggingEvent.getMDCPropertyMap().containsKey(
                    LoggingConstants.QUERY_KEY)) {
                String query = loggingEvent.getMDCPropertyMap()
                        .get(LoggingConstants.QUERY_KEY).trim();
                // Code stolen from ChklistSearchAction
                if (query.equals("")) {
                    family = null;
                    genus = null;
                    specificEpithet = null;
                } else {
                    String[] nameParts = query.toLowerCase().split(" ");
                    boolean validSearch = true;

                    for (int x = 0; x < nameParts.length; x++) {
                        if (nameParts[x] != null && nameParts[x].length() > 0) {
                            if (!nameParts[x].equals("*")) {
                                continue;
                            } else {
                                validSearch = false;
                                break;
                            }
                        } else {
                            validSearch = false;
                            break;
                        }
                    }

                    if (validSearch) {
                        switch (nameParts.length) {
                        case 0: // error, should be at least one term
                            family = null;
                            genus = null;
                            specificEpithet = null;
                            break;
                        case 1: // should be family or genus
                            if (nameParts[0].endsWith("aceae")) {
                                family = nameParts[0];
                                genus = null;
                                specificEpithet = null;
                            } else {
                                family = null;
                                genus = nameParts[0];
                                specificEpithet = null;
                            }
                            break;
                        case 2: // should be family & genus or genus & species
                            if (nameParts[0].endsWith("aceae")) {
                                family = nameParts[0];
                                genus = nameParts[1];
                                specificEpithet = null;
                            } else {
                                family = null;
                                genus = nameParts[0];
                                specificEpithet = nameParts[1];
                            }
                            break;
                        default:
                            if (nameParts[0].endsWith("aceae")) {
                                family = nameParts[0];
                                genus = nameParts[1];
                                specificEpithet = nameParts[2];
                            } else {
                                family = null;
                                genus = nameParts[0];
                                specificEpithet = nameParts[1];
                            }
                        }
                    } else {
                        family = null;
                        genus = null;
                        specificEpithet = null;
                    }
                }
            } else {
                if (loggingEvent.getMDCPropertyMap().containsKey(
                        LoggingConstants.FAMILY_KEY)) {
                    family = loggingEvent.getMDCPropertyMap()
                        .get(LoggingConstants.FAMILY_KEY);
                    genus = null;
                    specificEpithet = null;
                } else {
                    family = null;
                    genus = null;
                    specificEpithet = null;
                }
            }
           jdbcTemplate.update(new PreparedStatementCreator() {

            /**
             *
             */
            public PreparedStatement
            createPreparedStatement(final Connection connection)
                    throws SQLException {
                PreparedStatement preparedStatement =
                    connection.prepareStatement(insertSQL);

                Map<String, String> mergedMap = mergePropertyMaps(loggingEvent);
                bindParam(mergedMap, SEARCH_TYPE_INDEX,
                        LoggingConstants.SEARCH_TYPE_KEY, preparedStatement, 2);
                bindParam(mergedMap, IP_ADDRESS_INDEX,
                        ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY,
                        preparedStatement, IP_ADDRESS_LENGTH);
                preparedStatement.setString(CHECKLIST_INDEX,
                "selected_families");
                bindNullParam(REQ_REFERRER_INDEX, preparedStatement);
                bindParam(mergedMap, REQ_HOST_INDEX,
                        LoggingConstants.MDC_CLIENT_NAME_KEY, preparedStatement,
                        SEARCH_TYPE_LENGTH);
                bindNullParam(REMOTE_HOST_INDEX, preparedStatement);
                bindParam(family, FAMILY_INDEX, preparedStatement,
                        FAMILY_LENGTH);
                bindParam(genus, GENUS_INDEX, preparedStatement,
                        GENUS_LENGTH);
                bindParam(specificEpithet, SPECIES_INDEX, preparedStatement,
                        SPECIES_LENGTH);
                bindNullParam(AUTHOR_INDEX, preparedStatement);
                bindNullParam(INFRA_RANK_INDEX, preparedStatement);
                bindNullParam(INFRA_EPITHET_INDEX, preparedStatement);
                bindNullParam(PUBLICATION_INDEX, preparedStatement);
                bindNullParam(PUBLISHED_INDEX, preparedStatement);
                bindNullParam(TDWG_CODES_INDEX, preparedStatement);
                bindIntParam(mergedMap, RESULT_COUNT_INDEX,
                        LoggingConstants.RESULT_COUNT_KEY, preparedStatement);
                return preparedStatement;
            }
             });
        } catch (DataAccessException dae) {
            throw dae;
        }
    }

    /**
    *
    * @param index
    *            Set the index of the parameter of interest
    * @param stmt
    *            Set the prepared statement
    * @throws SQLException
    *             if something goes wrong
    */
   private void bindNullParam(final int index, final PreparedStatement stmt)
       throws SQLException {
       stmt.setString(index, "");
   }

    /**
     *
     * @param string
     *            the parameter to set
     * @param index
     *            Set the index of the parameter of interest
     * @param stmt
     *            Set the prepared statement
     * @param size
     *            the size of the field
     * @throws SQLException
     *             if something goes wrong
     */
    private void bindParam(final String string, final int index,
            final PreparedStatement stmt, final int size) throws SQLException {
        if (string != null) {
            if (string.length() > size) {
                stmt.setString(index, string.substring(0, size));
            } else {
                stmt.setString(index, string);
            }
        } else {
            stmt.setString(index, "");
        }
    }

    /**
     *
     * @param map
     *            the map of parameters
     * @param index
     *            Set the index of the parameter of interest
     * @param key
     *            Set the string of the parameter
     * @param stmt
     *            Set the prepared statement
     * @param size
     *            the size of the field
     * @throws SQLException
     *             if something goes wrong
     */
    private void bindParam(final Map<String, String> map, final int index,
            final String key, final PreparedStatement stmt, final int size)
            throws SQLException {
        if (map.containsKey(key)) {
            String value = map.get(key);
            if (value.length() > size) {
                stmt.setString(index, value.substring(0, size));
            } else {
                stmt.setString(index, value);
            }
        } else {
            stmt.setString(index, "");
        }
    }

    /**
     *
     * @param map
     *            the map of parameters
     * @param index
     *            Set the index of the parameter of interest
     * @param key
     *            Set the string of the parameter
     * @param stmt
     *            Set the prepared statement
     * @throws SQLException
     *             if something goes wrong
     */
    private void bindIntParam(final Map<String, String> map, final int index,
            final String key, final PreparedStatement stmt)
            throws SQLException {
        if (map.containsKey(key)) {
            stmt.setInt(index, Integer.parseInt(map.get(key)));
        } else {
            stmt.setInt(index, 0);
        }
    }

    /**
     *
     * @param event
     *            Set the logging event
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
     * @return the insertSQL
     */
    public final String getInsertSql() {
        return insertSQL;
    }

    /**
     * Set the statement you want to execute in order to log the request
     * This statement has to take 16 parameters. An example for mysql
     * 'insert into web_log (search_type,ip_address,checklist,req_referer,
     * req_host,remote_host,family,genus,species,author,infra_rank,infra_epithet
     * publication,published,tdwg_codes,result_count) values (?,?,?,?,?,?,?,?,?,
     * ?,?,?,?,?,?,?)'
     *
     * On sybase something like 'exec web_log_update @search_type=?,
     * @ip_address=?, @checklist=?, @req_referer=?,
     * @req_host=?, @remote_host=?, @family=?, @genus=?,
     * @species=?, @author=?, @infra_rank=?, @infra_epithet=?, @publication=?,
     * @published=?, @tdwg_codes=?, @result_count=?' might work
     *
     * @param newInsertSQL the insertSQL to set
     */
    public final void setInsertSql(final String newInsertSQL) {
        this.insertSQL = newInsertSQL;
    }
}
