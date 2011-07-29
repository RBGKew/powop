package org.emonocot.checklist.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;


import org.junit.Test;
import org.junit.runner.RunWith;
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
public class ChecklistTriggerIntegrationTest extends AbstractPersistenceTestSupport {

    /**
     *  Test an existing row being updated in the Plant_name table.
     * @throws SQLException if there is a problem executing the SQL
     */
    @Test
    public final void testUpdatePlantName() throws SQLException {
        jdbcTemplate.execute("UPDATE Plant_name SET Family = 'Loremaceae' WHERE Plant_name_id = 1");
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet("SELECT Date_modified, Date_Name_modified, Date_Locality_modified,"
                + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 1");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Date_modified should not be null",
                resultSet.getObject("Date_modified"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Date_modified should equal Date_Name_modified",
                resultSet.getObject("Date_modified"),
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
          "SELECT Date_modified, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified, Date_deleted from Plant_name_deleted where Plant_name_id = 1");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNull("Date_modified should be null",
                resultSet.getObject("Date_modified"));
        assertNull("Date_Name_modified should be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Date_modified should equal Date_Name_modified",
                resultSet.getObject("Date_modified"),
                resultSet.getObject("Date_Name_modified"));
        assertNotNull("Date_deleted should not be null",
                resultSet.getObject("Date_deleted"));

        resultSet = jdbcTemplate.queryForRowSet(
          "SELECT Date_modified, Date_Name_modified, Date_Locality_modified,"
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
          "SELECT Date_modified, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 2");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Date_modified should not be null",
                resultSet.getObject("Date_modified"));
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
        assertEquals("Date_modified should equal Date_Name_modified",
                resultSet.getObject("Date_modified"),
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
          "SELECT Date_modified, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 1");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Date_modified should not be null",
                resultSet.getObject("Date_modified"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNotNull("Date_Locality_modified should not be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Date_modified should equal Date_Name_modified",
                resultSet.getObject("Date_modified"),
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
          "SELECT Date_modified, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 2");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Date_modified should not be null",
                resultSet.getObject("Date_modified"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNotNull("Date_Locality_modified should not be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Date_modified should equal Date_Name_modified",
                resultSet.getObject("Date_modified"),
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
          "SELECT Date_modified, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 3");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Date_modified should not be null",
                resultSet.getObject("Date_modified"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNotNull("Date_Authors_modified should not be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Date_modified should equal Date_Name_modified",
                resultSet.getObject("Date_modified"),
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
          "SELECT Date_modified, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 1");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Date_modified should not be null",
                resultSet.getObject("Date_modified"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNotNull("Date_Authors_modified should not be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Date_modified should equal Date_Name_modified",
                resultSet.getObject("Date_modified"),
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
          "SELECT Date_modified, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 3");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Date_modified should not be null",
                resultSet.getObject("Date_modified"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNotNull("Date_Authors_modified should not be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNull("Date_Citations_modified should be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Date_modified should equal Date_Name_modified",
                resultSet.getObject("Date_modified"),
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
          "SELECT Date_modified, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 4");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Date_modified should not be null",
                resultSet.getObject("Date_modified"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNotNull("Date_Citations_modified should not be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Date_modified should equal Date_Name_modified",
                resultSet.getObject("Date_modified"),
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
          "SELECT Date_modified, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 1");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Date_modified should not be null",
                resultSet.getObject("Date_modified"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNotNull("Date_Citations_modified should not be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Date_modified should equal Date_Name_modified",
                resultSet.getObject("Date_modified"),
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
          "SELECT Date_modified, Date_Name_modified, Date_Locality_modified,"
        + " Date_Citations_modified, Date_Authors_modified FROM Plant_name WHERE Plant_name_id = 4");
        assertTrue("There should be one Plant_name row",
                resultSet.first());
        assertNotNull("Date_modified should not be null",
                resultSet.getObject("Date_modified"));
        assertNotNull("Date_Name_modified should not be null",
                resultSet.getObject("Date_Name_modified"));
        assertNull("Date_Locality_modified should be null",
                resultSet.getObject("Date_Locality_modified"));
        assertNull("Date_Authors_modified should be null",
                resultSet.getObject("Date_Authors_modified"));
        assertNotNull("Date_Citations_modified should not be null",
                resultSet.getObject("Date_Citations_modified"));
        assertEquals("Date_modified should equal Date_Name_modified",
                resultSet.getObject("Date_modified"),
                resultSet.getObject("Date_Name_modified"));
    }
}
