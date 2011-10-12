/**
 * 
 */
package org.emonocot.persistence.olap;

import static org.junit.Assert.*;

import javax.sql.DataSource;
import mondrian.olap.Axis;
import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Position;
import mondrian.olap.Query;
import mondrian.olap.Result;
import mondrian.olap.Util.PropertyList;
import mondrian.rolap.RolapConnectionProperties;

import org.emonocot.persistence.AbstractPersistenceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author jk00kg
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
public class DriverManagerTest extends AbstractPersistenceTest{

    /**
     * 
     */
    private PropertyList properties;
    /**
     * 
     */
    private Connection conx;
    /**
     * 
     */
    private DataSource dataSource;

    /**
     * @param mondrianConnection the conx to set
     */
//    public void setMondrianConnection(Connection mondrianConnection) {
//        this.conx = mondrianConnection;
//    }

    /**
     * @param dataSource
     *            the dataSource to set
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // Catalog name needs to be given from root directory/webapp root
        properties = new PropertyList();
        properties.put(RolapConnectionProperties.Catalog.name(),
                "target/test-classes/testMondrianSchema.xml");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createMondrianConnectionTest() {
        if (dataSource == null)
            fail("The context hasn't loaded");
        conx = DriverManager.getConnection(properties, null, dataSource);
        assertNotNull(conx);
    }

    public void queryTest() {

        // TODO make it work like a proper test
        Query query = conx
                .parseQuery("SELECT {[Measures].[number harvested]} on COLUMNS,"
                        + " {[taxa].members} on ROWS" + " FROM harvest");

        Result result = conx.execute(query);
        assertNotNull(result);
        Axis[] axes = result.getAxes();
        for (int i = 0; i < axes.length; i++) {
            for (Position pos : axes[i].getPositions()) {
                System.out.println(pos.size());
            }
        }
    }

}
