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
