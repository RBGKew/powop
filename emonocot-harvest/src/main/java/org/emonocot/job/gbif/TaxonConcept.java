package org.emonocot.job.gbif;

import java.util.List;
import java.util.ArrayList;

public class TaxonConcept {
	
	private List<TaxonName> hasName = new ArrayList<TaxonName>();
	
	public void setHasName(List<TaxonName> hasName) {
		this.hasName = hasName;
	}
	
	public List<TaxonName> getHasName() {
		return hasName;
	}
}
