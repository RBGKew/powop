package org.emonocot.pager;

import java.util.Comparator;


/**
 *
 * @author ben
 *
 */
public class FacetNameComparator implements Comparator<String> {

    /**
     * @param o1
     *            the first string
     * @param o2
     *            the second string
     * @return -1 if o1 comes before o2, 1 if o1 comes after o2 and 0 if o1 and
     *         o2 are equal
     */
    public final int compare(final String o1, final String o2) {
        FacetName fn1 = FacetName.fromString(o1);
        FacetName fn2 = FacetName.fromString(o2);
        return fn1.compareTo(fn2);
    }

}