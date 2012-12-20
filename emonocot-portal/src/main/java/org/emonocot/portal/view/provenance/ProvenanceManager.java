package org.emonocot.portal.view.provenance;

import java.util.Collection;
import java.util.SortedSet;

import org.emonocot.model.BaseData;
import org.emonocot.model.Taxon;
import org.emonocot.model.registry.Organisation;


public interface ProvenanceManager {
	
	/**
	 * Initializes the provenance manager at the start of the page
	 * @param taxon
	 */
	public void setProvenance(Taxon taxon);
	
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
