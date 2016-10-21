package org.emonocot.portal.view;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.emonocot.model.Taxon;
import org.emonocot.model.registry.Organisation;

import com.google.common.collect.Ordering;

public class ScientificNames {

	private Collection<Taxon> taxa;
	private Set<Organisation> sources;
	private List<Taxon> sorted;

	public ScientificNames(Collection<Taxon> taxa) {
		this.taxa = taxa;
		this.sources = new HashSet<>();
		for(Taxon taxon : taxa) {
			sources.add(taxon.getAuthority());
		}
	}

	public List<Taxon> getSorted() {
		if(sorted == null) {
			Ordering<Taxon> sortByName = new Ordering<Taxon>() {
				public int compare(Taxon t1, Taxon t2) {
					return t1.getScientificName().compareTo(t2.getScientificName());
				}
			};
			sorted = sortByName.sortedCopy(taxa);
		}
		return sorted;
	}

	public Set<Organisation> getSources() {
		return sources;
	}

	public int getCount() {
		return taxa.size();
	}
}
