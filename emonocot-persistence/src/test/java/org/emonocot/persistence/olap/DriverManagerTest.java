/**
 * 
 */
package org.emonocot.persistence.olap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.Callable;

import javax.sql.DataSource;

import mondrian.olap.Axis;
import mondrian.olap.Cell;
import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Position;
import mondrian.olap.Query;
import mondrian.olap.Result;
import mondrian.olap.Util.PropertyList;
import mondrian.rolap.RolapConnectionProperties;

import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.media.Image;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.AbstractPersistenceTest;
import org.emonocot.persistence.dao.TaxonDao;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jk00kg
 *
 */
public class DriverManagerTest extends AbstractPersistenceTest {

    /**
     *
     */
    private PropertyList properties;
    /**
     *
     */
    private DataSource dataSource;

    /**
     *
     */
    private Connection connection;

    /**
     *
     */
    private TaxonDao taxonDao;

    /**
     * @param taxonDao the taxonDao to set
     */
    @Autowired
    public final void setTaxonDao(final TaxonDao taxonDao) {
        this.taxonDao = taxonDao;
    }

    /**
     * @param newDataSource
     *            the dataSource to set
     */
    @Autowired
    public final void setDataSource(final DataSource newDataSource) {
        this.dataSource = newDataSource;
    }

    /**
     * @throws java.lang.Exception if there is a problem
     */
    @Before
    public final void setUp() throws Exception {
        // Catalog name needs to be given from root directory/webapp root
        properties = new PropertyList();
        properties.put(RolapConnectionProperties.Catalog.name(),
                "target/test-classes/testMondrianSchema.xml");
        properties.put(RolapConnectionProperties.PoolNeeded.name(), "true");

    }
    /**
     * @throws java.lang.Exception if there is a problem
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * @throws Exception if there is a problem setting up the test data
     */
    @Test
    public final void setUpTestData() throws Exception {
        doInTransaction(new Callable() {

            public Object call() {
                Taxon taxon1 = createTaxon("Aus",
                        "urn:lsid:example.com:taxon:1", null, null,
                        "Ausidae", "Aus", null, null, null, null, new GeographicalRegion[] {});
                createAnnotation(taxon1);
                Taxon taxon2 = createTaxon("Aus bus",
                        "urn:lsid:example.com:taxon:2", taxon1, null,
                        "Ausidae", "Aus", "bus", null, null, null, new GeographicalRegion[] {Continent.AUSTRALASIA,
                                Region.BRAZIL, Region.CARIBBEAN });
                createAnnotation(taxon2);
                Taxon taxon3 = createTaxon("Aus ceus",
                        "urn:lsid:example.com:taxon:3", taxon1, null,
                        "Ausidae", "Aus", "ceus", null, null, null, new GeographicalRegion[] {Region.NEW_ZEALAND });
                createAnnotation(taxon3);
                Taxon taxon4 = createTaxon("Aus deus",
                        "urn:lsid:example.com:taxon:4", null, taxon2,
                        "Ausidae", "Aus", "deus", null, null, null, new GeographicalRegion[] {});
                createAnnotation(taxon4);
                Taxon taxon5 = createTaxon("Aus eus",
                        "urn:lsid:example.com:taxon:5", null, taxon3,
                        "Ausidae", "Aus", "eus", null, null, null, new GeographicalRegion[] {});
                createAnnotation(taxon5);
                taxonDao.saveOrUpdate(taxon1);
                taxonDao.saveOrUpdate(taxon2);
                taxonDao.saveOrUpdate(taxon3);
                taxonDao.saveOrUpdate(taxon4);
                taxonDao.saveOrUpdate(taxon5);
                getSession().flush();
                return null;
            }
        });
    }
    /**
     *
     */
    @Test
    public final void createMondrianConnectionTest() {
        if (dataSource == null) {
            fail("The context hasn't loaded");
        }
        connection = DriverManager.getConnection(properties, null, dataSource);
        assertNotNull("The connection should not be null", connection);
    }

    /**
     *
     */
    @Test
    public final void queryTest() {
        connection = DriverManager.getConnection(properties, null, dataSource);
        assertNotNull("The connection should not be null", connection);
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
                axes[1].getPositions().get(1).get(0).getName(), "Ausidae");
        assertEquals("The value should be 5", result
                .getCell(new int[] {0, 1 }).getFormattedValue(), "5");
    }

}
