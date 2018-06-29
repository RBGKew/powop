package org.powo.portal.view;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.powo.model.Taxon;
import org.powo.model.registry.Organisation;

public class ScientificNames {

	private Set<Organisation> sources;
	private List<Taxon> sorted;

	Comparator<Taxon> byScientificName = Comparator.comparing(Taxon::getFamily)
			.thenComparing(Taxon::getGenus)
			.thenComparing(Taxon::getSpecificEpithet)
			.thenComparing(Taxon::getInfraspecificEpithet);

	public ScientificNames(Collection<Taxon> taxa) {
		this.sources = new HashSet<>();
		this.sorted = taxa.stream()
				.peek(taxon -> sources.add(taxon.getAuthority()))
				.sorted(byScientificName)
				.collect(Collectors.toList());
	}

	public List<Taxon> getSorted() {
		return sorted;
	}

	public Set<Organisation> getSources() {
		return sources;
	}

	public int getCount() {
		return sorted.size();
	}
}