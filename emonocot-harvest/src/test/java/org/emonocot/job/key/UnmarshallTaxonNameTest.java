package org.emonocot.job.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.transform.stax.StAXSource;

import org.junit.Test;
import org.tdwg.ubif.TaxonName;







/**
 *
 * @author ben
 *
 */
public class UnmarshallTaxonNameTest extends
        AbstractIdentificationKeyMarshallingTest {
    /**
     *
     */
    String filename
        = "/org/emonocot/job/sdd/TestKey_taxonNameFragment.xml";

    /**
     * @throws Exception
     *             if there is a problem
     *
     */
    @Test
    public final void testParseTaxonItemFragment() throws Exception {

        TaxonName taxonName = (TaxonName) super.getUnmarshaller()
            .unmarshal(new StAXSource(getXMLEventReader(filename)));
        assertNotNull("taxonName should not be null", taxonName);
        assertEquals("taxonName.id should equal t2", "t2", taxonName.getId());
        assertEquals("taxonName.representation.label should be Arum alpinariae", "Arum alpinariae", taxonName.getRepresentation().getLabel());
        assertEquals("taxonName.representation.mediaObject.ref should be m2", "m2", taxonName.getRepresentation().getMediaObject().getRef());
    }
}
