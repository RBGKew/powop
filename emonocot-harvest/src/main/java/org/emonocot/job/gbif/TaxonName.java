package org.emonocot.job.gbif;

public class TaxonName {
	
	private String nameComplete;
	
	private String genusPart;
	
	private String specificEpithet;
	
	private String authorship;
	
	private Boolean scientific;
	
	public void setNameComplete(String nameComplete) {
		this.nameComplete = nameComplete;
	}
	
	public String getNameComplete() {
		return nameComplete;
	}
	
	public void setSpecificEpithet(String specificEpithet) {
		this.specificEpithet = specificEpithet;
	}
	
	public String getSpecificEpithet() {
		return specificEpithet;
	}
	
	public void setAuthorship(String authorship) {
		this.authorship = authorship;
	}
	
	public String getAuthorship() {
		return authorship;
	}
	
	public void setScientific(Boolean scientific) {
		this.scientific = scientific;
	}
	
	public Boolean getScientific() {
		return scientific;
	}

	public String getGenusPart() {
		return genusPart;
	}

	public void setGenusPart(String genusPart) {
		this.genusPart = genusPart;
	}
}
