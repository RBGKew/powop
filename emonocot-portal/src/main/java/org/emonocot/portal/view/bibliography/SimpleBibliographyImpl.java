package org.emonocot.portal.view.bibliography;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.comparators.NullComparator;
import org.emonocot.model.Distribution;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.Description;

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
        for (Description d : taxon.getDescriptions()) {
            refs.addAll(d.getReferences());
        }
        for (Distribution d : taxon.getDistribution()) {
            refs.addAll(d.getReferences());
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

		private NullComparator nullSafeStringComparator = new NullComparator(
				new Comparator<String>() {

					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2);
					}

				});

        /**
         * @param o1
         *            Set the first reference
         * @param o2
         *            Set the second reference
         * @return 1 if o1 comes after o2, -1 if o1 comes before o2 and 0
         *         otherwise
         */
        public final int compare(final Reference o1, final Reference o2) {
        	int compareDate = nullSafeStringComparator.compare(o1.getDate(), o2.getDate());
        	if(compareDate == 0) {
        		int compareAuthor = nullSafeStringComparator.compare(o1.getCreator(), o2.getCreator());
        		if(compareAuthor == 0) {
        			int compareTitle = nullSafeStringComparator.compare(o1.getTitle(), o2.getTitle());
        			if(compareTitle == 0) {
        				int compareCitation = nullSafeStringComparator.compare(o1.getBibliographicCitation(), o2.getBibliographicCitation());
        				if(compareCitation == 0) {
        					return nullSafeStringComparator.compare(o1.getIdentifier(), o2.getIdentifier());
        				} else {
        					return compareCitation;
        				}
        			} else {
        				return compareTitle;
        			}
        		} else {
        			return compareAuthor;
        		}
        	} else {
        		return compareDate;
        	}
        }
    }

	@Override
	public SortedSet<String> getKeys(Collection<Distribution> distributions) {
		SortedSet<String> keys = new TreeSet<String>();
		for(Distribution d: distributions) {
			for(Reference r : d.getReferences()) {
				keys.add(getKey(r));
			}
		}
		return keys;
	}

	@Override
	public Reference getReference(String key) {
		Integer i = Integer.parseInt(key) - 1;
		return references.get(i);
	}
}
