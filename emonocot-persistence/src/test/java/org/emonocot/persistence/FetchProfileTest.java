package org.emonocot.persistence;

import static org.junit.Assert.assertTrue;

import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.Hibernate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class FetchProfileTest extends AbstractPersistenceTest {

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
                null, null, null, null, null, null, null,
                new GeographicalRegion[] {});
        Taxon taxon2 = createTaxon("Aus bus", "urn:lsid:example.com:taxon:2",
                taxon1, null, null, null, null, null, null, null,
                new GeographicalRegion[] {Continent.AUSTRALASIA,
                        Region.BRAZIL, Region.CARIBBEAN });
        Taxon taxon3 = createTaxon("Aus ceus", "urn:lsid:example.com:taxon:3",
                taxon1, null, null, null, null, null, null, null,
                new GeographicalRegion[] {Region.NEW_ZEALAND });
        Taxon taxon4 = createTaxon("Aus deus", "urn:lsid:example.com:taxon:4",
                null, taxon2, null, null, null, null, null, null,
                new GeographicalRegion[] {});
        Taxon taxon5 = createTaxon("Aus eus", "urn:lsid:example.com:taxon:5",
                null, taxon3, null, null, null, null, null, null,
                new GeographicalRegion[] {});
    }

    /**
     *
     */
    @Test
    public final void testFetchProfile() {
        Taxon taxon = getTaxonDao().load("urn:lsid:example.com:taxon:1",
                "taxon-page");
        assertTrue("Images should be initialized",
                Hibernate.isInitialized(taxon.getImages()));
    }
}
