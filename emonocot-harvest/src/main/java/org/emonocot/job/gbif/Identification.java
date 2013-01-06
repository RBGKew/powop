package org.emonocot.job.gbif;

import java.util.List;
import java.util.ArrayList;

public class Identification {
	
	private List<TaxonConcept> taxon = new ArrayList<TaxonConcept>();
	
	private String taxonName;
	
	public void setTaxonName(String taxonName) {
		this.taxonName = taxonName;
	}
	
	public String getTaxonName() {
		return taxonName;
	}
	
	public void setTaxon(List<TaxonConcept> taxon) {
		this.taxon = taxon;
	}
	
	public List<TaxonConcept> getTaxon() {
		return taxon;
	}
}
