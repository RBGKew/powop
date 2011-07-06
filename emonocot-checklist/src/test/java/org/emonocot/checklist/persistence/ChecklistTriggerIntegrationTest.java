package org.emonocot.checklist.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.dataset.filter.ExcludeTableFilter;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class ChecklistTriggerIntegrationTest {

   /**
    *
    */
   private static Logger logger
       = LoggerFactory.getLogger(ChecklistTriggerIntegrationTest.class);

   /**
    *
    */
   private Properties properties;

   /**
    *
    */
   private IDataTypeFactory dataTypeFactory = new DefaultDataTypeFactory();

    /**
     *
     */
    @Autowired
    private DataSource dataSource;

    /**
     *
     */
    JdbcTemplate jdbcTemplate;

    /**
     *
     * @throws IOException if there is a problem reading the properties file
     * @throws ClassNotFoundException
     *     if the data type factory class cannot be found
     * @throws IllegalAccessException if we do not have access to the
     *   data type factory class constructor
     * @throws InstantiationException if we cannot instantiate an instance of
     *   the data type factory
     * @throws SQLException if there is a problem executing the SQL
     * @throws DatabaseUnitException if there is a problem with DBUnit
     */
    @Before
    public final void setUp() throws IOException, ClassNotFoundException,
        InstantiationException, IllegalAccessException,
        DatabaseUnitException, SQLException {
        Resource propertiesFile
            = new ClassPathResource("application.properties");
        properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        String dataTypeFactoryClassName
            = (String) properties.get("dbunit.datatypefactory.class");
        if (dataTypeFactoryClassName != null) {
            Class dataTypeFactoryClass
               = Class.forName(dataTypeFactoryClassName);
            dataTypeFactory
                = (IDataTypeFactory) dataTypeFactoryClass.newInstance();
            jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(dataSource);
        }

        Resource dataSetFile
        = new ClassPathResource("org/emonocot/checklist/persistence/ChecklistTriggerIntegrationTest.xml");
        FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = dataSetBuilder.build(dataSetFile.getInputStream());
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            DatabaseOperation.CLEAN_INSERT.execute(getConnection(connection), dataSet);
        } catch (SQLException sqle) {
            DataSourceUtils.releaseConnection(connection, dataSource);
            connection = null;
            throw sqle;
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     *  Test an existing row being updated in the Plant_name table.
     * @throws SQLException if there is a problem executing the SQL
     */
    @Test
    public final void testUpdatePlantName() throws SQLException {
        jdbcTemplate.execute("UPDATE Plant_name SET Family = 'Loremaceae' WHERE Plant_name_id = 1");
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet("SELECT Modified_date, Date_Name_modified, Date_Locality_modified,"
                + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 1");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Modified_date should not be null",
                resultSet.getObject("Modified_date"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Modified_date should equal Date_Name_modified",
                resultSet.getObject("Modified_date"),
                resultSet.getObject("Date_Name_modified"));
    }

    /**
     *  Test an existing row being from the Plant_name table.
     * @throws SQLException if there is a problem executing the SQL
     */
    @Test
    public final void testDeletePlantName() throws SQLException {
        jdbcTemplate.execute("DELETE FROM Plant_name WHERE Plant_name_id = 1");
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(
          "SELECT Modified_date, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified, Deleted_date from Plant_name_deleted where Plant_name_id = 1");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNull("Modified_date should be null",
                resultSet.getObject("Modified_date"));
        assertNull("Date_Name_modified should be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Modified_date should equal Date_Name_modified",
                resultSet.getObject("Modified_date"),
                resultSet.getObject("Date_Name_modified"));
        assertNotNull("Deleted_date should not be null",
                resultSet.getObject("Deleted_date"));

        resultSet = jdbcTemplate.queryForRowSet(
          "SELECT Modified_date, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified from Plant_name where Plant_name_id = 1");
        assertFalse("There should be no Plant_name rows returned",
                resultSet.first());
    }

    /**
     *  Test an existing row being updated in the Plant_locality table.
     * @throws SQLException if there is a problem executing the SQL
     */
    @Test
    public final void testUpdatePlantLocality() throws SQLException {
        jdbcTemplate.execute(
        "UPDATE Plant_locality SET Area_code_L3 = 'SWE' WHERE Plant_locality_id = 1");

        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(
          "SELECT Modified_date, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 2");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Modified_date should not be null",
                resultSet.getObject("Modified_date"));
        /**
         * Hmmm. Setting the Date_locality_modified causes the
         * Plant_name update trigger to fire, setting
         * Plant_name_modified to NOW().
         * Should have seen that coming!
         */
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNotNull("Date_Locality_modified should not be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Modified_date should equal Date_Name_modified",
                resultSet.getObject("Modified_date"),
                resultSet.getObject("Date_Name_modified"));
    }

    /**
     *  Test inserting a new row into the Plant_locality table.
     * @throws SQLException if there is a problem executing the SQL
     */
    @Test
    public final void testInsertPlantLocality() throws SQLException {
        jdbcTemplate.execute(
        "INSERT INTO Plant_locality (Plant_name_id, Region_code_L2, Area_code_L3) VALUES (1,10,'GRB')");
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(
          "SELECT Modified_date, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 1");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Modified_date should not be null",
                resultSet.getObject("Modified_date"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNotNull("Date_Locality_modified should not be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Modified_date should equal Date_Name_modified",
                resultSet.getObject("Modified_date"),
                resultSet.getObject("Date_Name_modified"));
    }

    /**
     *  Test deleting an existing row from the Plant_locality table.
     * @throws SQLException if there is a problem executing the SQL
     */
    @Test
    public final void testDeletePlantLocality() throws SQLException {
        jdbcTemplate.execute(
        "DELETE FROM Plant_locality WHERE Plant_locality_id = 1");
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(
          "SELECT Modified_date, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 2");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Modified_date should not be null",
                resultSet.getObject("Modified_date"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNotNull("Date_Locality_modified should not be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Modified_date should equal Date_Name_modified",
                resultSet.getObject("Modified_date"),
                resultSet.getObject("Date_Name_modified"));
    }

    /**
     *  Test an existing row being updated in the Plant_author table.
     * @throws SQLException if there is a problem executing the SQL
     */
    @Test
    public final void testUpdatePlantAuthor() throws SQLException {
        jdbcTemplate.execute(
        "UPDATE Plant_author SET Author_type_id = 'COM' WHERE Plant_author_id = 1");
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(
          "SELECT Modified_date, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 3");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Modified_date should not be null",
                resultSet.getObject("Modified_date"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNotNull("Date_Authors_modified should not be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Modified_date should equal Date_Name_modified",
                resultSet.getObject("Modified_date"),
                resultSet.getObject("Date_Name_modified"));
    }

    /**
     *  Test inserting a new row into the Plant_author table.
     * @throws SQLException if there is a problem executing the SQL
     */
    @Test
    public final void testInsertPlantAuthor() throws SQLException {
        jdbcTemplate.execute(
        "INSERT INTO Plant_author (Plant_name_id, Author_type_id, Author_id) VALUES (1,'BAS',1)");
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(
          "SELECT Modified_date, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 1");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Modified_date should not be null",
                resultSet.getObject("Modified_date"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNotNull("Date_Authors_modified should not be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Modified_date should equal Date_Name_modified",
                resultSet.getObject("Modified_date"),
                resultSet.getObject("Date_Name_modified"));
    }

    /**
     *  Test deleting an existing row from the Plant_author table.
     * @throws SQLException if there is a problem executing the SQL
     */
    @Test
    public final void testDeletePlantAuthor() throws SQLException {
        jdbcTemplate.execute(
        "DELETE FROM Plant_author WHERE Plant_author_id = 1");
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(
          "SELECT Modified_date, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 3");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Modified_date should not be null",
                resultSet.getObject("Modified_date"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNotNull("Date_Authors_modified should not be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Modified_date should equal Date_Name_modified",
                resultSet.getObject("Modified_date"),
                resultSet.getObject("Date_Name_modified"));
    }

    /**
     *  Test an existing row being updated in the Plant_citation table.
     * @throws SQLException if there is a problem executing the SQL
     */
    @Test
    public final void testUpdatePlantCitation() throws SQLException {
        jdbcTemplate.execute(
        "UPDATE Plant_citation SET Publication_edition_id = 2 WHERE Plant_citation_id = 1");
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(
          "SELECT Modified_date, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 4");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Modified_date should not be null",
                resultSet.getObject("Modified_date"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNotNull("Date_Citations_modified should not be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Modified_date should equal Date_Name_modified",
                resultSet.getObject("Modified_date"),
                resultSet.getObject("Date_Name_modified"));
    }

    /**
     *  Test inserting a new row into the Plant_citation table.
     * @throws SQLException if there is a problem executing the SQL
     */
    @Test
    public final void testInsertPlantCitation() throws SQLException {
        jdbcTemplate.execute(
        "INSERT INTO Plant_citation (Plant_name_id, Publication_edition_id) VALUES (1,1)");
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(
          "SELECT Modified_date, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 1");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Modified_date should not be null",
                resultSet.getObject("Modified_date"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNotNull("Date_Citations_modified should not be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Modified_date should equal Date_Name_modified",
                resultSet.getObject("Modified_date"),
                resultSet.getObject("Date_Name_modified"));
    }

    /**
     *  Test deleting an existing row from the Plant_citation table.
     * @throws SQLException if there is a problem executing the SQL
     */
    @Test
    public final void testDeletePlantCitation() throws SQLException {
        jdbcTemplate.execute(
        "DELETE FROM Plant_citation WHERE Plant_citation_id = 1");
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(
          "SELECT Modified_date, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 4");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Modified_date should not be null",
                resultSet.getObject("Modified_date"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNotNull("Date_Citations_modified should not be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Modified_date should equal Date_Name_modified",
                resultSet.getObject("Modified_date"),
                resultSet.getObject("Date_Name_modified"));
    }

    /**
     *
     * @throws IOException if there is a problem reading the properties file
     * @throws SQLException if there is a problem executing the SQL
     * @throws DatabaseUnitException if there is a problem with DBUnit
     */
    @After
    public final void tearDown() throws IOException, DatabaseUnitException,
        SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            IDatabaseConnection databaseConnection = getConnection(connection);
            ITableFilter filter = new DatabaseSequenceFilter(databaseConnection);
            IDataSet dataSet = new FilteredDataSet(filter,
                    databaseConnection.createDataSet());
            DatabaseOperation.DELETE_ALL.execute(databaseConnection,
                    new FilteredDataSet(new ExcludeTableFilter(new String[] {
                            "databasechangelog", "databasechangeloglock" }),
                            dataSet));
        } catch (SQLException sqle) {
            DataSourceUtils.releaseConnection(connection, dataSource);
            connection = null;
            throw sqle;
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     * Returns an database connection object for use with DBUnit.
     *
     * @param connection Set the database connection
     * @return a database connection
     * @throws SQLException
     *             if there is a problem establishing a database connection
     */
    protected final IDatabaseConnection getConnection(
            final Connection connection) throws SQLException {
        IDatabaseConnection databaseConnection = null;
        try {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseConfig config = databaseConnection.getConfig();
            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                    dataTypeFactory);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return databaseConnection;
    }

    /**
     * Prints the data set to an output stream, using dbunit's
     * {@link org.dbunit.dataset.xml.FlatXmlDataSet}.
     * <p>
     * Remember, if you've just called save() or
     * update(), the data isn't written to the database until the
     * transaction is committed, and that isn't until after the
     * method exits. Consequently, if you want to test writing to
     * the database, either use the {@literal @ExpectedDataSet}
     * annotation (that executes after the test is run), or use
     * {@link CdmTransactionalIntegrationTest}.
     *
     * @param out The OutputStream to write to.
     * @throws IOException if there is a problem reading the properties file
     * @throws SQLException if there is a problem executing the SQL
     * @throws DataSetException if there is a problem with DBUnit
     * @see org.dbunit.dataset.xml.FlatXmlDataSet
     */
    public final void printDataSet(final OutputStream out) throws SQLException,
            DataSetException, IOException {
        Connection connection = DataSourceUtils.getConnection(dataSource);

        try {
            IDatabaseConnection databaseConnection = null;
            databaseConnection = getConnection(connection);
            IDataSet actualDataSet = databaseConnection.createDataSet();
            FlatXmlDataSet.write(actualDataSet, out);
        } catch (SQLException sqle) {
            DataSourceUtils.releaseConnection(connection, dataSource);
            connection = null;
            throw sqle;
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     * Prints the named tables to an output stream, using dbunit's
     * {@link org.dbunit.dataset.xml.FlatXmlDataSet}.
     *
     * @see {@link #printDataSet(OutputStream)}
     * @param out Set the output stream to write to
     * @param tableNames set the table names to write
     * @throws IOException if there is a problem reading the properties file
     * @throws SQLException if there is a problem executing the SQL
     * @throws DataSetException if there is a problem with DBUnit
     */
    public final void printDataSet(final OutputStream out,
            final String[] tableNames) throws SQLException, DataSetException,
            IOException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            IDatabaseConnection databaseConnection = null;
            databaseConnection = getConnection(connection);
            IDataSet actualDataSet = databaseConnection
                    .createDataSet(tableNames);
            FlatXmlDataSet.write(actualDataSet, out);
        } catch (SQLException sqle) {
            DataSourceUtils.releaseConnection(connection, dataSource);
            connection = null;
            throw sqle;
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     * Prints a dtd to an output stream, using dbunit's
     * {@link org.dbunit.dataset.xml.FlatDtdDataSet}.
     *
     * @param out The OutputStream to write to.
     * @throws IOException if there is a problem reading the properties file
     * @throws SQLException if there is a problem executing the SQL
     * @throws DataSetException if there is a problem with DBUnit
     * @see org.dbunit.dataset.xml.FlatDtdDataSet
     */
    public final void printDtd(final OutputStream out) throws SQLException,
            DataSetException, IOException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            IDatabaseConnection databaseConnection = null;
            databaseConnection = getConnection(connection);
            IDataSet actualDataSet = databaseConnection.createDataSet();
            FlatDtdDataSet.write(actualDataSet, out);
        } catch (SQLException sqle) {
            DataSourceUtils.releaseConnection(connection, dataSource);
            connection = null;
            throw sqle;
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }
}
