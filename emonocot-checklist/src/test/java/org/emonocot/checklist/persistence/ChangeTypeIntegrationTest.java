package org.emonocot.checklist.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.model.pager.Page;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext-test.xml" })
public class ChangeTypeIntegrationTest extends
        AbstractPersistenceTestSupport {

    @Autowired
    private TaxonDao taxonDao;

    @Test
    public void findCreatedTaxonTest() {
        assertNotNull("TaxonDAO should not be null", taxonDao);
        ChangeEvent<Taxon> result = taxonDao.find(Taxon.IDENTIFIER_PREFIX+1);
        assertNotNull(result);
        assertEquals("result ", ChangeType.CREATE, result.getType());
    }
    
    @Test
    public void findModifiedTaxonTest() {
        assertNotNull("TaxonDAO should not be null", taxonDao);
        ChangeEvent<Taxon> result = taxonDao.find(Taxon.IDENTIFIER_PREFIX+2);
        assertNotNull(result);
        assertEquals("result ", ChangeType.MODIFIED, result.getType());
    }

    @Test
    public void findDeletedTaxonTest() {
        assertNotNull("TaxonDAO should not be null", taxonDao);
        ChangeEvent<Taxon> result = taxonDao.find(Taxon.IDENTIFIER_PREFIX+3);
        assertNotNull(result);
        assertEquals("result ", ChangeType.DELETE, result.getType());
    }

    @Test
    public void findNothingTaxonTest() {
        assertNotNull("TaxonDAO should not be null", taxonDao);
        ChangeEvent<Taxon> result = taxonDao.find(Taxon.IDENTIFIER_PREFIX+4);
        assertNull(result);
    }
}
