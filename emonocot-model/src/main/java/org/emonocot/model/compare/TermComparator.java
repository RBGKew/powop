package org.emonocot.model.compare;

import java.util.Comparator;

import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.Term;

public class TermComparator implements Comparator<Term> {

	@Override
	public int compare(Term o1, Term o2) {
		if(o1.equals(DwcTerm.taxonID)) {
			return -1;
		} else if(o2.equals(DwcTerm.taxonID)) {
			return 1;
		} else {
			return o1.qualifiedName().compareTo(o2.qualifiedName());
		}
	}

}
