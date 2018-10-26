package org.powo.portal.view;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.powo.model.Taxon;
import org.powo.model.VernacularName;
import org.powo.model.registry.Organisation;

public class VernacularNames {

	class SortedCache {
		public SortedMap<String, SortedSet<String>> map;

		public SortedCache() {
			this.map = new TreeMap<>();
		}

		public void put(String key, String value) {
			if (!map.containsKey(key)) {
				map.put(key, new TreeSet<String>());
			}

			map.get(key).add(value);
		}
	}

	private Set<VernacularName> names;
	private SortedCache sorted;
	private Set<Organisation> sources;
	private Set<Taxon> synonymsIncluded;

	public VernacularNames(Taxon taxon) {
		names = new HashSet<>();
		synonymsIncluded = new HashSet<>();
		if (taxon.looksAccepted()) {
			names.addAll(taxon.getVernacularNames());
			for (Taxon synonym : taxon.getSynonymNameUsages()) {
				names.addAll(synonym.getVernacularNames());
			}

			sources = new HashSet<>();
			for (VernacularName name : names) {
				sources.add(name.getAuthority());
				if (!name.getTaxon().equals(taxon)) {
					synonymsIncluded.add(name.getTaxon());
				}
			}
		}
	}

	public Set<VernacularName> getNames() {
		return names;
	}

	public Set<Taxon> getSynonymsIncluded() {
		return synonymsIncluded;
	}

	public SortedMap<String, SortedSet<String>> getSortedByLanguage() {
		if (sorted == null) {
			sorted = new SortedCache();
			for (VernacularName name : names) {
				if (name.getLanguage() == null || name.getLanguage().equals(Locale.ROOT)) {
					sorted.put("Unknown", name.getVernacularName());
				} else {
					sorted.put(name.getLanguage().getDisplayName(), name.getVernacularName());
				}
			}
		}

		return sorted.map;
	}

	public Set<Organisation> getSources() {
		return this.sources;
	}
}
