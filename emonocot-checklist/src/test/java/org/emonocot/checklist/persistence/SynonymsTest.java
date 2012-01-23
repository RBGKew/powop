package org.emonocot.checklist.persistence;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.emonocot.checklist.model.Taxon;
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
@ContextConfiguration({ "/applicationContext-unitTest.xml" })
public class SynonymsTest extends AbstractPersistenceTestSupport {

    /**
     *
     */
    @Autowired
    private TaxonDao taxonDao;

    /**
     * A rather convoluted way of describing homotypic synonyms is tested here.
     */
    @Test
    public final void testGetSynonyms() {
        Taxon prime = taxonDao.get(1);
        Set<Taxon> synonyms = prime.getSynonyms();
        Set<Taxon> heterotypicSynonyms = prime.getHeterotypicSynonyms();
        Set<Taxon> homotypicSynonyms = prime.getHomotypicSynonyms();

        assertEquals("We should have 5 synonyms", 5, synonyms.size());
        assertEquals("We should have 2 heterotypic synonyms", 2,
                heterotypicSynonyms.size());
        assertEquals("We should have 2 homotypic synonyms", 2,
                homotypicSynonyms.size());
    }

}
