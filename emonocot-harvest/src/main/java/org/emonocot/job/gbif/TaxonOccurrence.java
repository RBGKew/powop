package org.emonocot.job.gbif;

import java.util.List;
import java.util.ArrayList;

public class TaxonOccurrence {
	
	private String basisOfRecordString;
    
    private String catalogNumber;
    
    private String collectionCode;
    
    private String collector;
    
    private String continent;
    
    private String stateProvince;
    
    private String country;
    
    private String institutionCode;
    
    private String earliestDateCollected;
    
    private String latestDateCollected;
    
    private String locality;
    
    private String gbifNotes;
    
    private String minimumElevationInMeters;
    
    private String maximumElevationInMeters;
    
    List<Identification> identifiedTo = new ArrayList<Identification>();
    
    public void setBasisOfRecordString(String basisOfRecordString) {
		this.basisOfRecordString = basisOfRecordString;
	}         
	
	public String getBasisOfRecordString() {
	    return basisOfRecordString;	
	}
	
	public void setCatalogNumber(String catalogNumber) {
		this.catalogNumber = catalogNumber;
	}
	
	public String getCatalogNumber() {
		return catalogNumber;
	}
	
	public void setCollectionCode(String collectionCode) {
		this.collectionCode = collectionCode;
	}
	
	public String getCollectionCode() {
		return collectionCode;
	}
	
	public void setCollector(String collector) {
		this.collector = collector;
	}
	
	public String getCollector() {
		return collector;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setInstitutionCode(String institutionCode) {
		this.institutionCode = institutionCode;
	}
	
	public String getInstitutionCode() {
		return institutionCode;
	}
	
	public void setEarliestDateCollected(String earliestDateCollected) {
		this.earliestDateCollected = earliestDateCollected;
	}
	
	public String getEarliestDateCollected() {
		return earliestDateCollected;
	}
	
	public void setLatestDateCollected(String latestDateCollected) {
		this.latestDateCollected = latestDateCollected;
	}
	
	public String getLatestDateCollected() {
		return latestDateCollected;
	}
	
	public void setLocality(String locality) {
		this.locality = locality;
	}
	
	public String getLocality() {
		return locality;
	}
	
	public void setContinent(String continent) {
		this.continent = continent;
	}
	
	public String getContinent() {
		return continent;
	}
	
	public void setIdentifiedTo(List<Identification> identifiedTo) {
		this.identifiedTo = identifiedTo;
	}
	
	public List<Identification> getIdentifiedTo() {
		return identifiedTo;
	}
	
	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}
	
	public String getStateProvince() {
		return stateProvince;
	}
	
	
	public void setMinimumElevationInMeters(String minimumElevationInMeters) {
		this.minimumElevationInMeters = minimumElevationInMeters;
	}
	
	public String getMinimumElevationInMeters() {
		return minimumElevationInMeters;
	}
	
	public void setMaximumElevationInMeters(String maximumElevationInMeters) {
		this.maximumElevationInMeters = maximumElevationInMeters;
	}
	
	public String getMaximumElevationInMeters() {
		return maximumElevationInMeters;
	}
}
