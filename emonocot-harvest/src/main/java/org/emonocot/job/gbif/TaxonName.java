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
