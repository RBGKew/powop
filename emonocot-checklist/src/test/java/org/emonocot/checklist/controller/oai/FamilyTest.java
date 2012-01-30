package org.emonocot.checklist.controller.oai;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.emonocot.checklist.model.Family;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.checklist.persistence.TaxonDao;
import org.emonocot.checklist.persistence.TaxonDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.openarchives.pmh.MetadataPrefix;

/**
 *
 * @author ben
 *
 */
public class FamilyTest {

    /**
     *
     */
    private OaiPmhController controller = new OaiPmhController();

    /**
     *
     */
    private TaxonDao taxonDao = null;

    /**
     *
     */
    @Before
    public final void setUp() {
        taxonDao = EasyMock.createMock(TaxonDao.class);
        controller.setTaxonService(taxonDao);
    }

    /**
     *
     */
    @Test
    public final void testGetFamilyRecord() {
        EasyMock.expect(taxonDao.getGenera(EasyMock.eq(Family.Lowiaceae)))
                .andReturn(new ArrayList<Taxon>());
        EasyMock.replay(taxonDao);
        try {
            controller.getRecord("urn:kew.org:wcs:family:28",
                    MetadataPrefix.OAI_DC);
        } catch (Exception e) {
            fail("No exception expected here");
        }
        EasyMock.verify(taxonDao);
    }

}
