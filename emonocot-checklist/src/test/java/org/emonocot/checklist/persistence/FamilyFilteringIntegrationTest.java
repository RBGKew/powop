package org.emonocot.checklist.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.SQLException;

import org.dbunit.dataset.DataSetException;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.model.pager.Page;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext-test.xml" })
public class FamilyFilteringIntegrationTest extends
        AbstractPersistenceTestSupport {

    /**
     *
     */
    @Autowired
    private TaxonDao taxonDao;

    /**
     *
     */
    @Test
    public final void nonFilteringTest() {
        assertNotNull("TaxonDAO should not be null", taxonDao);
        Page<ChangeEvent<Taxon>> result = taxonDao.page(null, null, null, null,
                null);
        assertEquals("page() should return five objects", Integer.valueOf(5),
                result.getSize());
    }

    /**
     *
     */
    @Test
    public final void familyFilteringWithValidFilterTermTest() {
        Page<ChangeEvent<Taxon>> result = taxonDao.page("Loreaceae", null,
                null, null, null);
        assertEquals("page() should return four objects", Integer.valueOf(4),
                result.getSize());
    }

    /**
     *
     */
    @Test
    public final void genusWildcardFilteringTest() {
    	Page<ChangeEvent<Taxon>> result = taxonDao.page("Loreaceae:L", null,
                null, null, null);
        assertEquals("page() should return three objects", Integer.valueOf(3),
                result.getSize());
    }

    /**
     *
     */
    @Test
    public final void familyFilteringWithInvalidFilterTermTest() {
        Page<ChangeEvent<Taxon>> result = taxonDao.page("Badgeraceae", null,
                null, null, null);
        assertEquals("page() should return no objects", Integer.valueOf(0),
                result.getSize());
    }
}
