package org.emonocot.checklist.persistence;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.filter.ExcludeTableFilter;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.model.pager.Page;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 *
 * @author ben
 *
 */
public class AbstractPersistenceTestSupport {

    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(ChecklistTriggerIntegrationTest.class);
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
    protected JdbcTemplate jdbcTemplate;

    /**
     *
     */
    public AbstractPersistenceTestSupport() {
        super();
    }

    /**
     *
     * @throws IOException
     *             if there is a problem reading the properties file
     * @throws ClassNotFoundException
     *             if the data type factory class cannot be found
     * @throws IllegalAccessException
     *             if we do not have access to the data type factory class
     *             constructor
     * @throws InstantiationException
     *             if we cannot instantiate an instance of the data type factory
     * @throws SQLException
     *             if there is a problem executing the SQL
     * @throws DatabaseUnitException
     *             if there is a problem with DBUnit
     */
    @Before
    public final void setUp() throws IOException, ClassNotFoundException,
            InstantiationException, IllegalAccessException,
            DatabaseUnitException, SQLException {
        Resource propertiesFile = new ClassPathResource(
                "application.properties");
        properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        String dataTypeFactoryClassName = (String) properties
                .get("dbunit.datatypefactory.class");
        if (dataTypeFactoryClassName != null) {
            Class dataTypeFactoryClass = Class
                    .forName(dataTypeFactoryClassName);
            dataTypeFactory = (IDataTypeFactory) dataTypeFactoryClass
                    .newInstance();
            jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(dataSource);
        }

        Resource dataSetFile = new ClassPathResource(
                "org/emonocot/checklist/persistence/"
                        + this.getClass().getSimpleName() + ".xml");
        FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = dataSetBuilder.build(dataSetFile.getInputStream());
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            DatabaseOperation.CLEAN_INSERT.execute(getConnection(connection),
                    dataSet);
        } catch (SQLException sqle) {
            DataSourceUtils.releaseConnection(connection, dataSource);
            connection = null;
            throw sqle;
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     *
     * @throws IOException
     *             if there is a problem reading the properties file
     * @throws SQLException
     *             if there is a problem executing the SQL
     * @throws DatabaseUnitException
     *             if there is a problem with DBUnit
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
     * @param connection
     *            Set the database connection
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
     * Remember, if you've just called save() or update(), the data isn't
     * written to the database until the transaction is committed, and that
     * isn't until after the method exits. Consequently, if you want to test
     * writing to the database, either use the {@literal @ExpectedDataSet}
     * annotation (that executes after the test is run), or use
     * {@link CdmTransactionalIntegrationTest}.
     *
     * @param out
     *            The OutputStream to write to.
     * @throws IOException
     *             if there is a problem reading the properties file
     * @throws SQLException
     *             if there is a problem executing the SQL
     * @throws DataSetException
     *             if there is a problem with DBUnit
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
     * @param out
     *            Set the output stream to write to
     * @param tableNames
     *            set the table names to write
     * @throws IOException
     *             if there is a problem reading the properties file
     * @throws SQLException
     *             if there is a problem executing the SQL
     * @throws DataSetException
     *             if there is a problem with DBUnit
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
     * @param out
     *            The OutputStream to write to.
     * @throws IOException
     *             if there is a problem reading the properties file
     * @throws SQLException
     *             if there is a problem executing the SQL
     * @throws DataSetException
     *             if there is a problem with DBUnit
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


    /**
     * Returns a subset of results based on a particular ChangeType  
     * @param type the ChangeType to include in the results returned
     * @param result the Page of ChangeEvents to inspect
     * @return
     */
    protected final List<ChangeEvent<Taxon>> listResults(ChangeType type,
            Page<ChangeEvent<Taxon>> result) {
        List<ChangeEvent<Taxon>> list = new ArrayList<ChangeEvent<Taxon>>();
        for (ChangeEvent<Taxon> event : result.getRecords()) {
            if (event.getType() == type)
                list.add(event);
        }
        return list;
    }
    /**
     *
     * @param expectedDataSetName the expected data set name
     * @throws Exception if there is a problem
     */
    public final void assertDatasetsEqual(
            final String expectedDataSetName) throws Exception {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            IDatabaseConnection databaseConnection = null;
            databaseConnection = getConnection(connection);
            IDataSet actualDataSet = databaseConnection.createDataSet();
            ClassPathResource classPathResource
                = new ClassPathResource(expectedDataSetName);
            IDataSet expectedDataSet
                = new FlatXmlDataSetBuilder().build(
                        classPathResource.getFile());
            for (String tableName : expectedDataSet.getTableNames()) {
                ITable expectedTable = expectedDataSet.getTable(tableName);
                ITable actualTable = actualDataSet.getTable(tableName);
                ITable filteredTable = DefaultColumnFilter.includedColumnsTable(
                        actualTable,
                        expectedTable.getTableMetaData().getColumns());
                Assertion.assertEquals(expectedTable, filteredTable);
            }

        } catch (Exception e) {
            DataSourceUtils.releaseConnection(connection, dataSource);
            connection = null;
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

}