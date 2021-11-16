package org.powo.job;

import java.util.ArrayList;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

/**
 * Extend this class to run integration tests which have the database cleared before each run. Differs
 * from {@link AbstractPersistenceTest} in that it does not provide explicit hooks for setup or helper
 * methods.
 * 
 * When you extend this class database setup occurs as follows:
 * 
 * 1 - Application starts up
 * 2 - Liquibase migrations run
 * 3 - Existing rows in all tables are deleted 
 * 4 - Your test method runs 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ 
	"/META-INF/spring/applicationContext.xml",
	"/META-INF/spring/applicationContext-batch.xml",
	"/META-INF/spring/applicationContext-jdbc-test.xml"
})
public abstract class AbstractDatabaseTest {
	private static final Logger log = LoggerFactory.getLogger(AbstractDatabaseTest.class);
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Before
	public void init() throws Exception {
		log.debug("running teardown");
		var tablesToDelete = tableNames();
		for (var table : tablesToDelete) {
			log.trace("deleting from table {}", table);
		}
		JdbcTestUtils.deleteFromTables(jdbcTemplate, tableNames());
	}

	private static final Set<String> blacklist = Set.of(
		// we keep the Liquibase tables so we don't need to migrate every time we run a test
		"databasechangelog", 
		"databasechangeloglock",
		// the following tables are used to generate sequences of IDs but are not actually a `SEQUENCE`
		// as far as MariaDB is concerned. Dropping them puts the database in an inconsistent state so
		// we keep them
		"batch_job_seq",
		"batch_job_execution_seq",
		"batch_step_execution_seq"
	);

	private String[] tableNames() throws MetaDataAccessException {
		return (String[]) JdbcUtils.extractDatabaseMetaData(dataSource, metadata -> {
			var tables = new ArrayList<String>();
			var rs = metadata.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				String tableName = rs.getString(3);
				if (!blacklist.contains(tableName)) {
					tables.add(tableName);
				}
			}
			return tables.toArray(new String[tables.size()]);
		});
	}
}
