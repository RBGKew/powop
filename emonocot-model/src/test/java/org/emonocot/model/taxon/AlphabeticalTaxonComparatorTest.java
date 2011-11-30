package org.emonocot.model.taxon;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /**
     *
     */
    private List<Taxon> taxa = new ArrayList<Taxon>();

    /**
     *
     */
    public final void setUp() {
        Taxon t1 = new Taxon();
        Taxon t2 = new Taxon();
        taxa.add(t1);
        taxa.add(t2);
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
     */
    @Test
    public final void testNullTaxa() {
        try {
            Collections.sort(taxa, comparator);
        } catch (Exception e) {
            fail("No exception expected here");
        }
    }
}
