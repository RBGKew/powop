package org.emonocot.portal.view;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.emonocot.model.Taxon;
import org.emonocot.model.VernacularName;
import org.emonocot.model.registry.Organisation;

public class VernacularNames {

	class SortedCache {
		public SortedMap<String, SortedSet<String>> map;

		public SortedCache() {
			this.map = new TreeMap<>();
		}

		public void put(String key, String value) {
			if(!map.containsKey(key)) {
				map.put(key, new TreeSet<String>());
			}

			map.get(key).add(value);
		}
	}

	private Set<VernacularName> names;
	private SortedCache sorted;
	private Set<Organisation> sources;

	public VernacularNames(Taxon taxon) {
		this.names = taxon.getVernacularNames();
		this.sources = new HashSet<>();
		for(VernacularName name :names) {
			sources.add(name.getAuthority());
		}
	}

	public Set<VernacularName> getNames() {
		return names;
	}

	public SortedMap<String, SortedSet<String>> getSortedByLanguage() {
		if(sorted == null) {
			sorted = new SortedCache();
			for(VernacularName name : names) {
				if(name.getLanguage().equals(Locale.ROOT)) {
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
