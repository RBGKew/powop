/**
 * 
 */
package org.emonocot.persistence.olap;


import static org.junit.Assert.*;

import java.io.File;

import javax.sql.DataSource;

import org.olap4j.OlapConnection;

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Util.PropertyList;
import mondrian.rolap.RolapConnectionProperties;
import mondrian.spi.CatalogLocator;
import mondrian.spi.impl.CatalogLocatorImpl;

import org.junit.After;
import org.junit.Before;
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
@ContextConfiguration(locations={"/applicationContext-test.xml"})
public class DriverManagerTest {

    /**
     * 
     */
    private PropertyList properties;
    /**
     * 
     */
    private DataSource dataSource;
    private Connection conx;

    /**
     * @param dataSource the dataSource to set
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
        //Catalog name needs to be given from root directory/webapp root
        properties = new PropertyList();
        properties.put(RolapConnectionProperties.Catalog.name(), "target/test-classes/testMondrianSchema.xml");
        properties.put(RolapConnectionProperties.PoolNeeded.name(), "true");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void createMondrianConnectionTest(){
        if(dataSource == null) fail("The flipping context hasn't loaded");
        conx = DriverManager.getConnection(properties, null, dataSource);
        assertNotNull(conx);
    }

}
