package org.emonocot.portal.view.provenance;

import org.emonocot.model.Taxon;
import org.emonocot.model.Description;
import org.emonocot.model.Distribution;
import org.emonocot.model.Image;
import org.emonocot.model.BaseData;
import org.emonocot.model.registry.Organisation;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class ProvenanceManagerImpl implements ProvenanceManager {
	
	Map<Organisation, SortedSet<ProvenanceHolderImpl>> provenance = new HashMap<Organisation,SortedSet<ProvenanceHolderImpl>>();
	List<ProvenanceHolderImpl> sortedProvenance = new ArrayList<ProvenanceHolderImpl>();
	
	
	@Override
	public void setProvenance(Taxon taxon) {
		addProvenance(taxon);
		for(Description description : taxon.getDescriptions()) {
			addProvenance(description);
		}
		for(Distribution distribution : taxon.getDistribution()) {
			addProvenance(distribution);
		}
		for(Organisation organisation : provenance.keySet()) {
			sortedProvenance.addAll(provenance.get(organisation));
		}
		
		for(ProvenanceHolderImpl provenanceHolder : sortedProvenance) {
			provenanceHolder.setKey(new String(Character.toChars(65 + sortedProvenance.indexOf(provenanceHolder))));
		}
	}
	
	private void addProvenance(BaseData data) {
		if(!provenance.containsKey(data.getAuthority())) {
			provenance.put(data.getAuthority(), new TreeSet<ProvenanceHolderImpl>());
		}
		provenance.get(data.getAuthority()).add(new ProvenanceHolderImpl(data));
	}
	
	
	@Override
	public String getKey(BaseData data) {
		ProvenanceHolderImpl provenanceHolder = new ProvenanceHolderImpl(data);
		return new String(Character.toChars(65 + sortedProvenance.indexOf(provenanceHolder)));
	}
	
	
	/*@Override
	public SortedSet<String> getKeys(Collection<BaseData> data){
		SortedSet<String> keys = new TreeSet<String>();
		for(BaseData baseData : data) {
			keys.add(getKey(baseData));
		}
		return keys;
	}*/
	
	
	@Override
	public SortedSet<Organisation> getSources() {
		SortedSet<Organisation> sources = new TreeSet<Organisation>();
		sources.addAll(provenance.keySet());
		return sources;
	}
	
	
	@Override
	public SortedSet<ProvenanceHolder> getProvenanceData(Organisation organization) {
		return (SortedSet)provenance.get(organization);
	}

}
