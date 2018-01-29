package org.powo.portal.view;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.powo.model.Taxon;
import org.powo.model.registry.Organisation;

import com.google.common.collect.Ordering;

public class ScientificNames {

	private Set<Organisation> sources;
	private List<Taxon> sorted;
	private Ordering<Taxon> sortByName = new Ordering<Taxon>() {
		public int compare(Taxon t1, Taxon t2) {
			return t1.getScientificName().compareTo(t2.getScientificName());
		}
	};

	public ScientificNames(Collection<Taxon> taxa) {
		this.sources = new HashSet<>();
		this.sorted = sortByName.sortedCopy(taxa);
		for(Taxon taxon : taxa) {
			sources.add(taxon.getAuthority());
		}
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
