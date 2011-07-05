package org.emonocot.checklist.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.dataset.datatype.IDataTypeFactory;
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
        }

        Resource dataSetFile
        = new ClassPathResource("org/emonocot/checklist/persistence/ChecklistTriggerIntegrationTest.xml");
        FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = dataSetBuilder.build(dataSetFile.getInputStream());
        DatabaseOperation.CLEAN_INSERT.execute(getConnection(), dataSet);
    }

    /**
     *  Test the insertion of a new record into the Plant_name table.
     * @throws SQLException if there is a problem executing the SQL
     */
    @Test
    public final void testUpdatePlantName() throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(
        "UPDATE Plant_name set Family = 'Loremaceae' where Plant_name_id = 1");
        statement.close();

        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
          "SELECT Modified_date, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified from Plant_name where Plant_name_id = 1");
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
        statement.close();
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
        Resource dataSetFile
        = new ClassPathResource("org/emonocot/checklist/persistence/ChecklistTriggerIntegrationTest.xml");
        FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = dataSetBuilder.build(dataSetFile.getInputStream());
        DatabaseOperation.DELETE_ALL.execute(getConnection(), dataSet);
    }

    /**
     * Returns an database connection object for use with DBUnit.
     *
     * @return a database connection
     * @throws SQLException
     *             if there is a problem establishing a database connection
     */
    protected final IDatabaseConnection getConnection() throws SQLException {
        IDatabaseConnection connection = null;
        try {
            connection = new DatabaseConnection(dataSource.getConnection());
            DatabaseConfig config = connection.getConfig();
            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                    dataTypeFactory);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return connection;
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
     * @see org.dbunit.dataset.xml.FlatXmlDataSet
     */
    public final void printDataSet(final OutputStream out) {
        IDatabaseConnection connection = null;

        try {
            connection = getConnection();
            IDataSet actualDataSet = connection.createDataSet();
            FlatXmlDataSet.write(actualDataSet, out);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException sqle) {
                logger.error(sqle.getMessage());
            }
        }
    }

    /**
     * Prints the named tables to an output stream, using dbunit's
     * {@link org.dbunit.dataset.xml.FlatXmlDataSet}.
     *
     * @see {@link #printDataSet(OutputStream)}
     * @param out Set the output stream to write to
     * @param tableNames set the table names to write
     */
    public final void printDataSet(final OutputStream out,
            final String[] tableNames) {
        IDatabaseConnection connection = null;

        try {
            connection = getConnection();
            IDataSet actualDataSet = connection.createDataSet(tableNames);
            FlatXmlDataSet.write(actualDataSet, out);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException sqle) {
                logger.error(sqle.getMessage());
            }
        }
    }

    /**
     * Prints a dtd to an output stream, using dbunit's
     * {@link org.dbunit.dataset.xml.FlatDtdDataSet}.
     *
     * @param out The OutputStream to write to.
     * @see org.dbunit.dataset.xml.FlatDtdDataSet
     */
    public final void printDtd(final OutputStream out) {
        IDatabaseConnection connection = null;

        try {
            connection = getConnection();
            IDataSet actualDataSet = connection.createDataSet();
            FlatDtdDataSet.write(actualDataSet, out);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException sqle) {
                logger.error(sqle.getMessage());
            }
        }
    }
}
