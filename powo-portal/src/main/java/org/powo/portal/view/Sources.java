package org.powo.portal.view;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import org.powo.model.BaseData;
import org.powo.model.Description;
import org.powo.model.Distribution;
import org.powo.model.Identifier;
import org.powo.model.Image;
import org.powo.model.MeasurementOrFact;
import org.powo.model.Taxon;
import org.powo.model.TypeAndSpecimen;
import org.powo.model.VernacularName;
import org.powo.model.registry.Organisation;

import com.google.common.collect.ComparisonChain;

public class Sources {

	public class License {
		public String license;
		public String rights;
		public String key;

		public License(String licence, String rights) {
			this.license = licence;
			this.rights = rights;
		}

		public boolean equals(Object o) {
			if(o instanceof License) {
				License other = (License)o;
				return Objects.equals(this.license, other.license)
						&& Objects.equals(this.rights, other.rights);
			} else {
				return false;
			}
		}

		public int hashCode() {
			return Objects.hash(license, rights);
		}
	}

	public class Source implements Comparable<Source> {
		public Set<License> licenses;
		public Organisation organisation;

		public Source(BaseData data) {
			this.licenses = new HashSet<>();
			this.organisation = data.getAuthority();

			licenses.add(new License(data.getLicense(), data.getRights()));
		}

		@Override
		public boolean equals(Object other) {
			if(other instanceof Source) {
				Source otherSource = (Source)other;
				return Objects.equals(organisation.getIdentifier(), otherSource.organisation.getIdentifier());
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(organisation.getIdentifier());
		}

		@Override
		public int compareTo(Source other) {
			return ComparisonChain.start()
					.compare(organisation.getIdentifier(), other.organisation.getIdentifier())
					.result();
		}
	}

	Map<Source, Source> sources = new TreeMap<>();

	public Sources(Taxon taxon) {
		addSource(taxon);
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

		// images and descriptions are not shown on synonym pages
		if(taxon.looksAccepted()) {
			for (Image images : taxon.getImages()) {
				addSource(images);
			}
			for (Description description : taxon.getDescriptions()) {
				addSource(description);
			}
		}

		int i = 0;
		for(Source source : sources.values()) {
			for(License license : source.licenses) {
				license.key = new String(Character.toChars(65 + i++));
			}
		}
	}

	private void addSource(BaseData data) {
		Source source = new Source(data);
		if(sources.containsKey(source)) {
			sources.get(source).licenses.add(new License(data.getLicense(), data.getRights()));
		} else {
			sources.put(source, source);
		}
	}

	public Set<Source> getSorted() {
		return sources.keySet();
	}
}