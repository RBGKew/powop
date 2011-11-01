package org.emonocot.persistence.olap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.AbstractPersistenceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.OlapConnection;
import org.olap4j.layout.CellSetFormatter;
import org.olap4j.layout.RectangularCellSetFormatter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jk00kg
 *
 */
public class OlapConnectionFactoryTest extends AbstractPersistenceTest {

    /**
     *
     */
    @Autowired
    private OlapConnection olapConnection;

    /**
     *
     */
    @Autowired
    private DataSource dataSource;

    /**
     * @throws java.lang.Exception if there is a problem
     */
    @Before
    public final void setUp() throws Exception {
        super.doSetUp();       
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
                null, new GeographicalRegion[] {});
        createAnnotation(taxon1);
        Taxon taxon2 = createTaxon("Aus bus", "urn:lsid:example.com:taxon:2",
                taxon1, null, "Ausidae", "Aus", "bus", null, null, null,
                null, new GeographicalRegion[] {Continent.AUSTRALASIA,
                        Region.BRAZIL, Region.CARIBBEAN });
        createAnnotation(taxon2);
        Taxon taxon3 = createTaxon("Aus ceus", "urn:lsid:example.com:taxon:3",
                taxon1, null, "Ausidae", "Aus", "ceus", null, null, null,
                null, new GeographicalRegion[] {Region.NEW_ZEALAND });
        createAnnotation(taxon3);
        Taxon taxon4 = createTaxon("Aus deus", "urn:lsid:example.com:taxon:4",
                null, taxon2, "Ausidae", "Aus", "deus", null, null, null,
                null, new GeographicalRegion[] {});
        createAnnotation(taxon4);
        Taxon taxon5 = createTaxon("Aus eus", "urn:lsid:example.com:taxon:5",
                null, taxon3, "Ausidae", "Aus", "eus", null, null, null,
                null, new GeographicalRegion[] {});
        createAnnotation(taxon5);
    }
    /**
     *
     */
    @Test
    public final void createConnection() {
        try {
          assertNotNull("The connection should not be null", olapConnection);
        } catch(Exception e) {
            fail("No exception expected here");
        }
    }

    /**
     *
     */
    @Test
    public final void queryTest() throws Exception {        
        CellSet result = olapConnection.createStatement().executeOlapQuery("SELECT {[Measures].[Object Numbers]} on COLUMNS,"
                    + " {[taxa].[family].members} on ROWS" + " FROM Job");

        assertNotNull(result);
        List<CellSetAxis> axes = result.getAxes();
        CellSetFormatter cellSetFormatter = new RectangularCellSetFormatter(true);
        PrintWriter writer = new PrintWriter(System.out);
        cellSetFormatter.format(result,  writer);
        writer.flush();
        assertEquals("The label should be Ausidae",
                axes.get(1).getPositions().get(0).getMembers().get(0).getName(), "Ausidae");
        List<Integer> index = new ArrayList<Integer>();
        index.add(0);
        index.add(0);
        assertEquals("The value should be 5", result
                .getCell(index).getFormattedValue(), "5");
    }

}
