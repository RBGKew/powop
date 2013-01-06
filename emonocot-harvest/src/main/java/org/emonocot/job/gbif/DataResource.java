package org.emonocot.job.gbif;

import java.util.ArrayList;
import java.util.List;

public class DataResource {
	
	private String name;
	
	private String rights;
	
	private String citation;
	
	private List<TaxonOccurrence> occurrenceRecords = new ArrayList<TaxonOccurrence>();
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setRights(String rights) {
		this.rights = rights;
	}
	
	public String getRights() {
		return rights;
	}
	
	public void setCitation(String citation) {
		this.citation = citation;
	}
	
	public String getCitation() {
		return citation;
	}
	
	public void setOccurrenceRecords(List<TaxonOccurrence> occurrenceRecords) {
		this.occurrenceRecords = occurrenceRecords;
	}
	
	public List<TaxonOccurrence> getOccurrenceRecords() {
		return occurrenceRecords;
	}
	
}
