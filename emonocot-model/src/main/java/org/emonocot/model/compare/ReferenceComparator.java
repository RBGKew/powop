package org.emonocot.model.compare;

import java.util.Comparator;

import org.apache.commons.collections.comparators.NullComparator;
import org.emonocot.model.Reference;

public class ReferenceComparator implements Comparator<Reference> {
	
	private NullComparator nullSafeStringComparator;
	
	public ReferenceComparator() {
		Comparator<String> stringComparator = new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}

		};
		nullSafeStringComparator = new NullComparator(stringComparator, false);
	}

	@Override
	public int compare(Reference o1, Reference o2) {
		int compareDate = nullSafeStringComparator.compare(o1.getDate(), o2.getDate());
    	if(compareDate == 0) {
    		int compareAuthor = nullSafeStringComparator.compare(o1.getCreator(), o2.getCreator());
    		if(compareAuthor == 0) {
    			int compareTitle = nullSafeStringComparator.compare(o1.getTitle(), o2.getTitle());
    			if(compareTitle == 0) {
    				return nullSafeStringComparator.compare(o1.getBibliographicCitation(), o2.getBibliographicCitation());        				
    			} else {
    				return compareTitle;
    			}
    		} else {
    			return compareAuthor;
    		}
    	} else {
    		return -1 * compareDate;
    	}
	}

}
