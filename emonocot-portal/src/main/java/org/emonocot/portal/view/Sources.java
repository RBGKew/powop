package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import org.emonocot.model.BaseData;
import org.emonocot.model.Description;
import org.emonocot.model.Distribution;
import org.emonocot.model.Identifier;
import org.emonocot.model.Image;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.Taxon;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.model.VernacularName;
import org.emonocot.model.registry.Organisation;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class Sources {

	public class Source implements Comparable<Source> {
		public String rights;
		public String license;
		public String key;
		public Organisation organisation;

		public Source(BaseData data) {
			this.rights = data.getRights();
			this.license = data.getLicense();
			this.organisation = data.getAuthority();
		}

		@Override
		public boolean equals(Object other) {
			if(other instanceof Source) {
				Source otherSource = (Source)other;
				return Objects.equals(organisation.getIdentifier(), otherSource.organisation.getIdentifier())
						&& Objects.equals(license, otherSource.license)
						&& Objects.equals(rights, otherSource.rights);
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(organisation.getIdentifier(), license, rights);
		}

		@Override
		public int compareTo(Source other) {
			return ComparisonChain.start()
					.compare(organisation.getIdentifier(), other.organisation.getIdentifier())
					.compare(license, other.license, Ordering.natural().nullsLast())
					.compare(rights, other.rights, Ordering.natural().nullsLast())
					.result();
		}
	}

	SortedSet<Organisation> organisations = new TreeSet<>();
	Map<String, SortedSet<Source>> sources = new HashMap<>();
	List<Source> sortedSources = new ArrayList<>();

	public Sources(Taxon taxon) {
		addSource(taxon);
		for (Image images : taxon.getImages()) {
			addSource(images);
		}
		for (Description description : taxon.getDescriptions()) {
			addSource(description);
		}
		for (Distribution distribution : taxon.getDistribution()) {
			addSource(distribution);
		}
		for (Taxon childNameUsages : taxon.getChildNameUsages()) {
			addSource(childNameUsages);
		}
		for (Taxon synonymNameUsages : taxon.getSynonymNameUsages()) {
			addSource(synonymNameUsages);
		}
		for(Identifier identifier : taxon.getIdentifiers()) {
			addSource(identifier);
		}
		for (TypeAndSpecimen typesAndSpecimens : taxon.getTypesAndSpecimens()) {
			addSource(typesAndSpecimens);
		}
		for (VernacularName vernacularName : taxon.getVernacularNames()) {
			addSource(vernacularName);
		}
		for (Taxon higherClassification : taxon.getHigherClassification()) {
			addSource(higherClassification);
		}
		for (MeasurementOrFact measurementOrFact : taxon.getMeasurementsOrFacts()) {
			addSource(measurementOrFact);
		}

		for(Organisation organisation : organisations) {
			sortedSources.addAll(sources.get(organisation.getIdentifier()));
		}

		for(Source source : sortedSources) {
			source.key = new String(Character.toChars(65 + sortedSources.indexOf(source)));
		}
	}

	private void addSource(BaseData data) {
		if(!sources.keySet().contains(data.getAuthority().getIdentifier())) {
			organisations.add(data.getAuthority());
			sources.put(data.getAuthority().getIdentifier(), new TreeSet<Source>());
		}

		sources.get(data.getAuthority().getIdentifier()).add(new Source(data));
	}

	public String getKey(BaseData data) {
		Source source = new Source(data);
		return new String(Character.toChars(65 + sortedSources.indexOf(source)));
	}

	public SortedSet<String> getKeys(Collection<BaseData> data){
		SortedSet<String> keys = new TreeSet<String>();
		for(BaseData baseData : data) {
			keys.add(getKey(baseData));
		}
		return keys;
	}

	public List<Source> getSorted() {
		return sortedSources;
	}
}