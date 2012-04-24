package org.emonocot.model.taxon;

import java.util.Comparator;

/**
 *
 * @author ben
 *
 */
public class RankBasedTaxonComparator implements Comparator<Taxon> {

    /**
     * @param o1
     *            the first taxon
     * @param o2
     *            the second taxon
     * @return -1 if o1 comes before o2, 1 if o1 comes after o2 and 0 if the two
     *         regions are equal
     */
    public final int compare(final Taxon o1, final Taxon o2) {
        int o = 0;
        /**
         * Nulls last
         */
        if (o1.getRank() == null) {
            if (o1.getRank() != o2.getRank()) {
                return 1;
            } else {
                return 0;
            }
        } else if (o2.getRank() == null) {
            return -1;
        } else {
            o = o1.getRank().compareTo(o2.getRank());
        }

        /**
         * Homonyms
         */
        if (o == 0) {
            if (o1.getName() == null) {
                if (o1.getName() != o2.getName()) {
                    return 1;
                } else {
                    return 0;
                }
            } else if (o2.getName() == null) {
                return -1;
            } else {
                return o1.getName().compareTo(o2.getName());
            }
        } else {
            return o;
        }
    }

}
