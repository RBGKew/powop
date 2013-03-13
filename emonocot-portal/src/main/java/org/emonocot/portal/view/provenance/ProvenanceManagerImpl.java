package org.emonocot.portal.view.provenance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.emonocot.model.BaseData;
import org.emonocot.model.Description;
import org.emonocot.model.Distribution;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Image;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.Taxon;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.model.VernacularName;
import org.emonocot.model.registry.Organisation;

public class ProvenanceManagerImpl implements ProvenanceManager {
	SortedSet<Organisation> organisations = new TreeSet<Organisation>();
	
	Map<String, SortedSet<ProvenanceHolderImpl>> provenance = new HashMap<String,SortedSet<ProvenanceHolderImpl>>();
	List<ProvenanceHolderImpl> sortedProvenance = new ArrayList<ProvenanceHolderImpl>();
	
	
	@Override
	public void setProvenance(Taxon taxon) {
		addProvenance(taxon);
		for(Image images : taxon.getImages()) {
			addProvenance(images);
		}
		for(Description description : taxon.getDescriptions()) {
			addProvenance(description);
		}
		for(Distribution distribution : taxon.getDistribution()) {
			addProvenance(distribution);
		}
		for(Taxon childNameUsages : taxon.getChildNameUsages()) {
			addProvenance(childNameUsages);
		}
		for(Taxon synonymNameUsages : taxon.getSynonymNameUsages()) {
			addProvenance(synonymNameUsages);
		}
		for(TypeAndSpecimen typesAndSpecimens : taxon.getTypesAndSpecimens()) {
			addProvenance(typesAndSpecimens);
		}
		for(VernacularName vernacularName : taxon.getVernacularNames()) {
			addProvenance(vernacularName);
		}
		for(Taxon higherClassification : taxon.getHigherClassification()) {
			addProvenance(higherClassification);
		}
		for(MeasurementOrFact measurementOrFact : taxon.getMeasurementsOrFacts()) {
			addProvenance(measurementOrFact);
		}
		for(IdentificationKey key : taxon.getKeys()) {
			addProvenance(key);
		}
		
		for(Organisation organisation : organisations) {
			sortedProvenance.addAll(provenance.get(organisation.getIdentifier()));
		}
		
		for(ProvenanceHolderImpl provenanceHolder : sortedProvenance) {
			provenanceHolder.setKey(new String(Character.toChars(65 + sortedProvenance.indexOf(provenanceHolder))));
		}
	}
	
	private void addProvenance(BaseData data) {
		if(!provenance.keySet().contains(data.getAuthority().getIdentifier())) {
			organisations.add(data.getAuthority());
			provenance.put(data.getAuthority().getIdentifier(), new TreeSet<ProvenanceHolderImpl>());
		}
		
		provenance.get(data.getAuthority().getIdentifier()).add(new ProvenanceHolderImpl(data));
	}
	
	
	@Override
	public String getKey(BaseData data) {
		ProvenanceHolderImpl provenanceHolder = new ProvenanceHolderImpl(data);
		return new String(Character.toChars(65 + sortedProvenance.indexOf(provenanceHolder)));
	}
	
	
	@Override
	public SortedSet<String> getKeys(Collection<BaseData> data){
		SortedSet<String> keys = new TreeSet<String>();
		for(BaseData baseData : data) {
			keys.add(getKey(baseData));
		}
		return keys;
	}
	
	
	@Override
	public SortedSet<Organisation> getSources() {
		return organisations;
	}
	
	
	@Override
	public SortedSet<ProvenanceHolder> getProvenanceData(Organisation organization) {
		return (SortedSet)provenance.get(organization.getIdentifier());
	}

}
