/**
 * 
 */
package org.emonocot.persistence.olap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.sql.DataSource;

import mondrian.olap.Axis;
import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Query;
import mondrian.olap.Result;
import mondrian.olap.Util.PropertyList;
import mondrian.rolap.RolapConnection;
import mondrian.rolap.RolapConnectionProperties;

import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.AbstractPersistenceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jk00kg
 *
 */
public class RolapConnectionFactoryTest extends AbstractPersistenceTest {

    private RolapConnectionFactory rolapConnectionFactory;
    
    @Autowired
    private DataSource dataSource;

    

    /**
     * @throws java.lang.Exception if there is a problem
     */
    @Before
    public final void setUp() throws Exception {
        super.doSetUp();
        rolapConnectionFactory = new RolapConnectionFactory();
        rolapConnectionFactory.setDataSource(dataSource);
        rolapConnectionFactory.setCatalogName("target/classes/olap.xml");
        rolapConnectionFactory.setPoolNeeded(Boolean.TRUE);
    }
    /**
     * @throws java.lang.Exception if there is a problem
     */
    @After
    public final void tearDown() throws Exception {
        super.doTearDown();
    }

    /**
     *
     */
    @Override
    public final void setUpTestData() {
        Taxon taxon1 = createTaxon("Aus", "urn:lsid:example.com:taxon:1", null,
                null, "Ausidae", "Aus", null, null, null, null,
                new GeographicalRegion[] {});
        createAnnotation(taxon1);
        Taxon taxon2 = createTaxon("Aus bus", "urn:lsid:example.com:taxon:2",
                taxon1, null, "Ausidae", "Aus", "bus", null, null, null,
                new GeographicalRegion[] {Continent.AUSTRALASIA,
                        Region.BRAZIL, Region.CARIBBEAN });
        createAnnotation(taxon2);
        Taxon taxon3 = createTaxon("Aus ceus", "urn:lsid:example.com:taxon:3",
                taxon1, null, "Ausidae", "Aus", "ceus", null, null, null,
                new GeographicalRegion[] {Region.NEW_ZEALAND });
        createAnnotation(taxon3);
        Taxon taxon4 = createTaxon("Aus deus", "urn:lsid:example.com:taxon:4",
                null, taxon2, "Ausidae", "Aus", "deus", null, null, null,
                new GeographicalRegion[] {});
        createAnnotation(taxon4);
        Taxon taxon5 = createTaxon("Aus eus", "urn:lsid:example.com:taxon:5",
                null, taxon3, "Ausidae", "Aus", "eus", null, null, null,
                new GeographicalRegion[] {});
        createAnnotation(taxon5);
    }
    /**
     *
     */
    @Test
    public final void createConnection() {
        try {
          assertNotNull("The connection should not be null", rolapConnectionFactory.getObject());
        } catch(Exception e) {
            fail("No exception expected here");
        }
    }

    /**
     *
     */
    @Test
    public final void queryTest() throws Exception {
        Connection connection = rolapConnectionFactory.getObject();
        Query query = connection
                .parseQuery("SELECT {[Measures].[number harvested]} on COLUMNS,"
                    + " {[taxa].[family].members} on ROWS" + " FROM harvest");

        Result result = connection.execute(query);
        assertNotNull(result);
        Axis[] axes = result.getAxes();
//        for (int i = 0; i < axes[1].getPositions().size(); i++) {
//            Cell cell = result.getCell(new int[] {0,i});
//              System.out.println(axes[1].getPositions().get(i).get(0) + " " + cell.getValue());
//        }
        assertEquals("The label should be Ausidae",
                axes[1].getPositions().get(0).get(0).getName(), "Ausidae");
        assertEquals("The value should be 5", result
                .getCell(new int[] {0, 0 }).getFormattedValue(), "5");
    }

}
