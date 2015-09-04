/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.job.gbif;

import java.util.ArrayList;
import java.util.List;

public class TaxonOccurrence {

	private String gbifKey;

	private String about;

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

	private String county;

	private String gbifNotes;

	private String minimumElevationInMeters;

	private String maximumElevationInMeters;

	private String rights;

	private String rightsHolder;

	private String accessRights;

	private String license;

	private String bibliographicCitation;

	private String coordinateUncertaintyInMeters;

	private Double decimalLatitude;

	private Double decimalLongitude;

	private String maximumDepthInMeters;

	private String minimumDepthInMeters;

	private String identifier;

	private String source;

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

	public String getRights() {
		return rights;
	}

	public String getGbifKey() {
		return gbifKey;
	}

	public void setGbifKey(String gbifKey) {
		this.gbifKey = gbifKey;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getGbifNotes() {
		return gbifNotes;
	}

	public void setGbifNotes(String gbifNotes) {
		this.gbifNotes = gbifNotes;
	}

	public String getRightsHolder() {
		return rightsHolder;
	}

	public void setRightsHolder(String rightsHolder) {
		this.rightsHolder = rightsHolder;
	}

	public String getAccessRights() {
		return accessRights;
	}

	public void setAccessRights(String accessRights) {
		this.accessRights = accessRights;
	}

	public String getBibliographicCitation() {
		return bibliographicCitation;
	}

	public void setBibliographicCitation(String bibliographicCitation) {
		this.bibliographicCitation = bibliographicCitation;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCoordinateUncertaintyInMeters() {
		return coordinateUncertaintyInMeters;
	}

	public void setCoordinateUncertaintyInMeters(
			String coordinateUncertaintyInMeters) {
		this.coordinateUncertaintyInMeters = coordinateUncertaintyInMeters;
	}

	public Double getDecimalLatitude() {
		return decimalLatitude;
	}

	public void setDecimalLatitude(Double decimalLatitude) {
		this.decimalLatitude = decimalLatitude;
	}

	public Double getDecimalLongitude() {
		return decimalLongitude;
	}

	public void setDecimalLongitude(Double decimalLongitude) {
		this.decimalLongitude = decimalLongitude;
	}

	public String getMaximumDepthInMeters() {
		return maximumDepthInMeters;
	}

	public void setMaximumDepthInMeters(String maximumDepthInMeters) {
		this.maximumDepthInMeters = maximumDepthInMeters;
	}

	public String getMinimumDepthInMeters() {
		return minimumDepthInMeters;
	}

	public void setMinimumDepthInMeters(String minimumDepthInMeters) {
		this.minimumDepthInMeters = minimumDepthInMeters;
	}

}
