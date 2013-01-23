package org.emonocot.model.util;

import java.util.Comparator;

import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DwcTerm;

public class ConceptTermComparator implements Comparator<ConceptTerm> {

	@Override
	public int compare(ConceptTerm o1, ConceptTerm o2) {
		if(o1.equals(DwcTerm.taxonID)) {
			return -1;
		} else if(o2.equals(DwcTerm.taxonID)) {
			return 1;
		} else {
			return o1.qualifiedName().compareTo(o2.qualifiedName());
		}
	}

}
