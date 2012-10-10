package org.emonocot.pager;

import java.util.Comparator;

import org.emonocot.api.FacetName;

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
        FacetName facetName1 = FacetName.valueOf(o1);
        FacetName facetName2 = FacetName.valueOf(o2);
        return facetName1.compareTo(facetName2);
    }

}
