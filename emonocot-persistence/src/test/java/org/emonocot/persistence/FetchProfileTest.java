package org.emonocot.persistence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;

import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.media.Image;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.TaxonDao;
import org.hibernate.Hibernate;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
public class FetchProfileTest extends AbstractPersistenceTest {

    /**
     *
     */
    @Autowired
    private TaxonDao taxonDao;

    /**
     * @throws Exception if there is a problem with the callable
     */
    @Test
    public final void setUpTestDataWithinTransaction() throws Exception {

        doInTransaction(new Callable() {
            public Object call() {
                FullTextSession fullTextSession = Search
                        .getFullTextSession(getSession());
                fullTextSession.purgeAll(Taxon.class);
                fullTextSession.purgeAll(Image.class);
                Taxon taxon1 = createTaxon("Aus",
                        "urn:lsid:example.com:taxon:1", null, null,
                        null, null, null, null, null, null, new GeographicalRegion[] {});
                Taxon taxon2 = createTaxon("Aus bus",
                        "urn:lsid:example.com:taxon:2", taxon1, null,
                        null, null, null, null, null, null, new GeographicalRegion[] {Continent.AUSTRALASIA,
                                Region.BRAZIL, Region.CARIBBEAN });
                Taxon taxon3 = createTaxon("Aus ceus",
                        "urn:lsid:example.com:taxon:3", taxon1, null,
                        null, null, null, null, null, null, new GeographicalRegion[] {Region.NEW_ZEALAND });
                Taxon taxon4 = createTaxon("Aus deus",
                        "urn:lsid:example.com:taxon:4", null, taxon2,
                        null, null, null, null, null, null, new GeographicalRegion[] {});
                Taxon taxon5 = createTaxon("Aus eus",
                        "urn:lsid:example.com:taxon:5", null, taxon3,
                        null, null, null, null, null, null, new GeographicalRegion[] {});
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
    public final void testFetchProfile() {
        assertNotNull("taxonDao should not be null", taxonDao);
        Taxon taxon = taxonDao.load("urn:lsid:example.com:taxon:1",
                "taxon-page");
        assertTrue("Images should be initialized",
                Hibernate.isInitialized(taxon.getImages()));
    }
}
