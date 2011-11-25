package org.emonocot.model.taxon;

import java.util.Comparator;

/**
 *
 * @author ben
 *
 */
public class AlphabeticalTaxonComparator implements Comparator<Taxon> {

    /**
     * @param o1 the first taxon
     * @param o2 the second taxon
     * @return -1 if o1 comes before o2, 1 if o1 comes after o2 and 0 if the two
     *         regions are equal
     */
    public final int compare(final Taxon o1, final Taxon o2) {
        int o = 0;
        /**
         * Nulls last
         */
        if (o1.getName() == null) {
            if (o1.getName() != o2.getName()) {
                o = 1;
            }
        }
        if (o2.getName() == null) {
            o = -1;
        }
        o = o1.getName().compareTo(o2.getName());

        /**
         * Homonyms
         */
        if (o == 0) {
            return o1.getAuthorship().compareTo(o2.getAuthorship());
        } else {
            return o;
        }
    }

}
