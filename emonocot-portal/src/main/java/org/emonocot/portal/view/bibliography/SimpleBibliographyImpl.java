package org.emonocot.portal.view.bibliography;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.emonocot.model.description.TextContent;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;

/**
 *
 * @author ben
 *
 */
public class SimpleBibliographyImpl implements Bibliography {
    /**
     *
     */
    List<Reference> references = new ArrayList<Reference>();

   /**
    *
    * @param taxon Set the taxon
    */
    public final void setReferences(final Taxon taxon) {
        SortedSet<Reference> refs = new TreeSet<Reference>(
                new ReferenceComparator());
        refs.addAll(taxon.getReferences());
        for (TextContent t : taxon.getContent().values()) {
            refs.addAll(t.getReferences());
        }
        references.addAll(refs);
    }

   /**
    *
    * @param reference Set the reference
    * @return A string key which can be displayed in the taxon page as a
    *         citation reference to the reference
    */
    public final String getKey(final Reference reference) {
        return new Integer(references.indexOf(reference) + 1).toString();
    }

   /**
    *
    * @return a sorted list of references
    */
    public final List<Reference> getReferences() {
        return references;
    }

    /**
     *
     * @author ben
     *
     */
    class ReferenceComparator implements Comparator<Reference> {

        /**
         * @param o1
         *            Set the first reference
         * @param o2
         *            Set the second reference
         * @return 1 if o1 comes after o2, -1 if o1 comes before o2 and 0
         *         otherwise
         */
        public final int compare(final Reference o1, final Reference o2) {
            if (o1.getDatePublished() == null) {
                if (o2.getDatePublished() == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if (o2.getDatePublished() == null) {
                    return 1;
                } else {
                    int compareDate = o1.getDatePublished().compareTo(
                            o2.getDatePublished());
                    if (compareDate == 0) {
                        /**
                         * TODO Could implement more sophisticated sorting but
                         * probably not worth it right now
                         */
                        return 0;
                    } else {
                        return compareDate;
                    }
                }
            }
        }
    }
}
