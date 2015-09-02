/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.persistence.spatial;

/**
 * This class is taken from the course Core Spring by SpringSource. The source
 * can be used legally, but credits go to the guys from Spring.
 *
 * This code was development at Geodan (http://www.geodan.com).
 *
 * @author janb (jan.boonen@geodan.nl)
 */

import geodb.GeoDB;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * A factory that creates a data source fit for use in a system test
 * environment. Creates a simple data source that connects to an in-memory
 * database pre-loaded with test data.
 *
 * This factory returns a fully-initialized DataSource implementation. When the
 * DataSource is returned, callers are guaranteed that the database schema and
 * test data will have been loaded by that time.
 *
 * Is a FactoryBean, for exposing the fully-initialized test DataSource as a
 * Spring bean. See {@link #getObject()}.
 */
public class GeoDBTestDataSourceFactory implements FactoryBean<Object>,
InitializingBean {

	/**
	 *
	 */
	private static Logger logger = LoggerFactory
			.getLogger(GeoDBTestDataSourceFactory.class);

	/**
	 *
	 */
	private String testDatabaseName;

	/**
	 *
	 */
	private Resource schemaLocation;

	/**
	 *
	 */
	private List<Resource> schemaLocations;

	/**
	 *
	 */
	private Resource testDataLocation;

	/**
	 * The object created by this factory.
	 */
	private DataSource dataSource;

	/**
	 * Creates a new TestDataSourceFactory for use in "bean" style. "Bean" style
	 * means the default constructor is called and then properties are set to
	 * configure this object. "Bean" style usage is nice when this object is
	 * defined as a Spring bean, as setter-injection can be more descriptive
	 * than constructor-injection from the point of view of a bean definition
	 * author.
	 *
	 * @see {@link #setTestDatabaseName(String)}
	 * @see {@link #setSchemaLocation(Resource)}
	 * @see {@link #setTestDataLocation(Resource)}
	 */
	public GeoDBTestDataSourceFactory() {
	}

	/**
	 * Creates a new TestDataSourceFactory fully-initialized with what it needs
	 * to work. Fully-formed constructors are nice in a programmatic
	 * environment, as they result in more concise code and allow for a class to
	 * enforce its required properties.
	 *
	 * @param databaseName
	 *            the name of the test database to create
	 * @param schemaLoc
	 *            the location of the file containing the schema DDL to export
	 *            to the database
	 * @param dataLocation
	 *            the location of the file containing the test data to load into
	 *            the database
	 */
	public GeoDBTestDataSourceFactory(final String databaseName,
			final Resource schemaLoc, final Resource dataLocation) {
		setTestDatabaseName(databaseName);
		setSchemaLocation(schemaLoc);
		setTestDataLocation(dataLocation);
	}

	/**
	 * Sets the name of the test database to create.
	 *
	 * @param databaseName
	 *            the name of the test database, i.e. "rewards"
	 */
	public final void setTestDatabaseName(final String databaseName) {
		this.testDatabaseName = databaseName;
	}

	/**
	 * Sets the location of the file containing the schema DDL to export to the
	 * test database.
	 *
	 * @param schemaLoc
	 *            the location of the database schema DDL
	 */
	public final void setSchemaLocation(final Resource schemaLoc) {
		this.schemaLocation = schemaLoc;
	}

	/**
	 * Sets the location of the file containing the test data to load into the
	 * database.
	 *
	 * @param dataLocation
	 *            the location of the test data file
	 */
	public final void setTestDataLocation(final Resource dataLocation) {
		this.testDataLocation = dataLocation;
	}

	/**
	 *
	 * @param schemaLoc set the locations of the schema
	 */
	public final void setSchemaLocations(final List<Resource> schemaLoc) {
		this.schemaLocations = schemaLoc;
	}

	/**
	 * this method is automatically called by Spring after configuration to
	 * perform a dependency check and init.
	 */
	public final void afterPropertiesSet() {
		if (testDatabaseName == null) {
			throw new IllegalArgumentException(
					"The name of the test database to create is required");
		}
		if (schemaLocation == null
				&& (schemaLocations == null || schemaLocations.isEmpty())) {
			throw new IllegalArgumentException(
					"The path to the database schema DDL is required");
		}
		initDataSource();
	}

	/**
	 * implementing FactoryBean.
	 * this method is automatically called by Spring to expose the DataSource as
	 * a bean
	 * @return the datasource
	 * @throws Exception if there is a problem
	 */
	public final Object getObject() throws Exception {
		return getDataSource();
	}

	/**
	 * @return the class of object
	 */
	public final Class<?> getObjectType() {
		return DataSource.class;
	}

	/**
	 * @return true if the object is a singleton
	 */
	public final boolean isSingleton() {
		return true;
	}

	/**
	 * Factory method that returns the fully-initialized test data source.
	 * Useful when this class is used programatically instead of deployed as a
	 * Spring bean.
	 *
	 * @return the data source
	 */
	public final DataSource getDataSource() {
		if (dataSource == null) {
			initDataSource();
		}
		return dataSource;
	}

	// static factory methods

	/**
	 * Static factory method that creates a DataSource that connects to a test
	 * database populated with test data.
	 *
	 * @param testDatabaseName
	 *            the name of the test database to create
	 * @param schemaLocation
	 *            the database schema to export
	 * @param testDataLocation
	 *            the database test data to load
	 * @return the data source
	 */
	public static DataSource createDataSource(final String testDatabaseName,
			final Resource schemaLocation, final Resource testDataLocation) {
		return new GeoDBTestDataSourceFactory(testDatabaseName, schemaLocation,
				testDataLocation).getDataSource();
	}

	/**
	 * encapsulates the steps involved in initializing the data source: creating
	 * it, and populating it.
	 */
	private void initDataSource() {
		// create the in-memory database source first
		this.dataSource = createDataSource();
		if (logger.isDebugEnabled()) {
			logger.debug("Created in-memory test database '" + testDatabaseName
					+ "'");
		}
		// now populate the database by loading the schema and test data
		populateDataSource();
		if (logger.isDebugEnabled()) {
			logger.debug("Exported schema in " + schemaLocation);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Loaded test data in " + testDataLocation);
		}
	}

	/**
	 *
	 * @return the datasource
	 */
	private DataSource createDataSource() {
		DriverManagerDataSource driverManagerDataSource
		= new DriverManagerDataSource();
		// use the HsqlDB JDBC driver
		driverManagerDataSource.setDriverClassName("org.h2.Driver");
		// have it create an in-memory database
		driverManagerDataSource.setUrl("jdbc:h2:mem:" + testDatabaseName
				+ ";DB_CLOSE_DELAY=-1");
		return driverManagerDataSource;
	}

	/**
	 *
	 */
	private void populateDataSource() {
		TestDatabasePopulator populator = new TestDatabasePopulator(dataSource);
		populator.populate();
	}

	/**
	 * Populates a in memory data source with test data.
	 */
	private class TestDatabasePopulator {

		/**
		 *
		 */
		private DataSource dataSource;

		/**
		 * Creates a new test database populator.
		 *
		 * @param source
		 *            the test data source that will be populated.
		 */
		public TestDatabasePopulator(final DataSource source) {
			this.dataSource = source;
		}

		/**
		 * Populate the test database by creating the database schema from
		 * 'schema.sql' and inserting the test data in 'testdata.sql'.
		 */
		public void populate() {
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				createDatabaseSchema(connection);
				if (testDataLocation != null) {
					insertTestData(connection);
				}
			} catch (SQLException e) {
				throw new RuntimeException(
						"SQL exception occurred acquiring connection", e);
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						// do nothing
					}
				}
			}
		}

		/**
		 * create the application's database schema (tables, indexes, etc.).
		 * @param connection Set the connection
		 */
		private void createDatabaseSchema(final Connection connection) {
			try {
				// add spatial capabilities to database:
				GeoDB.InitGeoDB(connection);
				if (schemaLocation != null) {
					String sql = parseSqlIn(schemaLocation);
					executeSql(sql, connection);
				} else {
					for (Resource location : schemaLocations) {
						String sql = parseSqlIn(location);
						executeSql(sql, connection);
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(
						"I/O exception occurred accessing the database schema file",
						e);
			} catch (SQLException e) {
				throw new RuntimeException(
						"SQL exception occurred exporting database schema", e);
			}
		}

		/**
		 * populate the tables with test data.
		 *
		 * @param connection Set the connection
		 */
		private void insertTestData(final Connection connection) {
			try {
				String sql = parseSqlIn(testDataLocation);
				executeSql(sql, connection);
			} catch (IOException e) {
				throw new RuntimeException(
						"I/O exception occurred accessing the test data file",
						e);
			} catch (SQLException e) {
				throw new RuntimeException(
						"SQL exception occurred loading test data", e);
			}
		}

		/**
		 * utility method to read a .sql txt input stream.
		 *
		 * @param resource Set the resource
		 * @return the parsed SQL
		 * @throws IOException if there is a problem reading the resource file
		 */
		private String parseSqlIn(final Resource resource) throws IOException {
			InputStream is = null;
			try {
				is = resource.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				StringWriter sw = new StringWriter();
				BufferedWriter writer = new BufferedWriter(sw);

				for (int c = reader.read(); c != -1; c = reader.read()) {
					writer.write(c);
				}
				writer.flush();
				return sw.toString();

			} finally {
				if (is != null) {
					is.close();
				}
			}
		}

		/**
		 * utility method to run the parsed sql.
		 *
		 * @param sql Set the SQL
		 * @param connection Set the connection
		 * @throws SQLException if there is a problem executing the supplied SQL
		 */
		private void executeSql(final String sql, final Connection connection)
				throws SQLException {
			Statement statement = connection.createStatement();
			statement.execute(sql);
		}
	}
}
