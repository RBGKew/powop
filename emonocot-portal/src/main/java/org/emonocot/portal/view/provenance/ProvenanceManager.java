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
package org.emonocot.portal.view.provenance;

import java.util.Collection;
import java.util.SortedSet;

import org.emonocot.model.BaseData;
import org.emonocot.model.registry.Organisation;


public interface ProvenanceManager {
	
	/**
	 * Initializes the provenance manager at the start of the page
	 * @param taxon
	 */
	public void setProvenance(BaseData data);
	
	/**
	 * Get a label for the particular provenance of this object. Should be unique for each combination of source, rights and license
	 * @param data
	 * @return
	 */
	public String getKey(BaseData data);
	
	/**
	 * Get a labels for the particular provenance of this object. Should be unique for each combination of source, rights and license
	 * @param data
	 * @return
	 */
	public SortedSet<String> getKeys(Collection<BaseData> data);
	
	
	/**
	 * Get the sorted list of organizations 
	 * @return
	 */
	public SortedSet<Organisation> getSources();
	
	/**
	 * Get the sorted list of provenance holder objects for a single organization
	 * @param organization
	 * @return
	 */
	public SortedSet<ProvenanceHolder> getProvenanceData(Organisation organization);

}
