package org.emonocot.model.util;

import static org.junit.Assert.fail;

import org.emonocot.model.Taxon;
import org.emonocot.model.util.AlphabeticalTaxonComparator;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class AlphabeticalTaxonComparatorTest {
    /**
     *
     */
    private AlphabeticalTaxonComparator comparator = new AlphabeticalTaxonComparator();

    private Taxon t1 = null;
    
    private Taxon t2 = null;

    /**
     *
     */
    @Before
    public final void setUp() {
        t1 = new Taxon();
        t2 = new Taxon();
        t2.setScientificName("Poa annua");
    }

    /**
     * Tests the case where Taxon.name == null (which did result in a
     * NullPointerException).
     * http://build.e-monocot.org/bugzilla/show_bug.cgi?id=125 Null Pointer
     * Exception in AlphabeticTaxonComparator java.lang.NullPointerException:
     * java.lang.String.compareTo(String.java:1168)
     * org.emonocot.model.taxon.AlphabeticalTaxonComparator
     * .compare(AlphabeticalTaxonComparator.java:19)
     * org.emonocot.model.taxon.AlphabeticalTaxonComparator
     * .compare(AlphabeticalTaxonComparator.java:10)
     *
     * Also bug http://build.e-monocot.org/bugzilla/show_bug.cgi?id=149
     */
    @Test
    public final void testNullTaxa() {
        try {
            comparator.compare(t1, t2);
        } catch (Exception e) {
            fail("No exception expected here");
        }
    }
}
