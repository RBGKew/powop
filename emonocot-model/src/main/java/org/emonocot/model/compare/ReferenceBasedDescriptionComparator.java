package org.emonocot.model.compare;

import java.util.Comparator;

import org.emonocot.model.Description;
import org.emonocot.model.Reference;

public class ReferenceBasedDescriptionComparator implements	Comparator<Description> {
	
	Comparator<Reference> referenceComparator = new ReferenceComparator();

	@Override
	public int compare(Description o1, Description o2) {
		if(o1.getReferences().isEmpty() || o2.getReferences().isEmpty()) {
			return 0;
		} else {
			return referenceComparator.compare(o1.getReferences().iterator().next(), o2.getReferences().iterator().next());			
		}
	}

}
