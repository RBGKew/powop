package org.powo.portal.view;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import org.powo.model.BaseData;
import org.powo.model.Taxon;
import org.powo.model.registry.Organisation;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Streams;

public class Sources {

	public static class License {
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

		public boolean isNotBlank() {
			return !(isNullOrEmpty(license) && isNullOrEmpty(rights));
		}
	}

	public static class Source implements Comparable<Source> {
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
		Streams.concat(
				taxon.getDistribution().stream(),
				taxon.getChildNameUsages().stream(),
				taxon.getSynonymNameUsages().stream(),
				taxon.getIdentifiers().stream(),
				taxon.getTypesAndSpecimens().stream(),
				taxon.getHigherClassification().stream(),
				taxon.getMeasurementsOrFacts().stream())
		.forEach(bd -> addSource(bd));

		// some data is pulled from synonyms onto accepted pages
		if(taxon.looksAccepted()) {
			handleSources(taxon);
			for (Taxon synonym : taxon.getSynonymNameUsages()) {
				handleSources(synonym);
			}
		}

		int i = 0;
		for(Source source : sources.values()) {
			for(License license : source.licenses) {
				license.key = new String(Character.toChars(65 + i++));
			}
		}
	}

	private void handleSources(Taxon taxon) {
		Streams.concat(
				taxon.getImages().stream(),
				taxon.getDescriptions().stream(),
				taxon.getIdentifications().stream(),
				taxon.getVernacularNames().stream())
		.forEach(bd -> addSource(bd));
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