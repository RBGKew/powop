/**
 * 
 */
package org.emonocot.model.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.emonocot.model.media.Image;
import org.emonocot.model.taxon.Taxon;
import org.junit.Test;

/**
 * @author jk00kg
 *
 */
public class TaxonomyBridgeTest {

    /**
     * Test method for {@link org.emonocot.model.hibernate.TaxonomyBridge#reflectiveGetTaxon(java.lang.Object, java.lang.String[])}.
     */
    @Test
    public void testReflectiveGetTaxon() {
        TaxonomyBridge testClass = new TaxonomyBridge();
        Taxon expected = new Taxon();
        expected.setIdentifier("1");
        Image i = new Image();
        i.setIdentifier("i1");
        i.setTaxon(expected);
        Object o = new Object();
        
        Taxon actual = testClass.reflectiveGetTaxon(o);
        assertNull("Oject.java should not have a taxon", actual);
        
        actual = testClass.reflectiveGetTaxon(i);
        assertEquals("The image should have returned the expected Taxon", expected, actual);
    }

}
