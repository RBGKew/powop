package org.emonocot.portal.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.emonocot.model.Distribution;
import org.emonocot.model.Taxon;
import org.emonocot.model.registry.Organisation;

import com.google.common.collect.Ordering;

public class Distributions {

	private Set<Distribution> distributions;
	private List<Distribution> sorted;
	private Set<Organisation> sources;

	public Distributions(Taxon taxon) {
		this.distributions = taxon.getDistribution();
		this.sources = new HashSet<>();
		for(Distribution d : distributions) {
			sources.add(d.getAuthority());
		}
	}

	public List<Distribution> getSorted() {
		if(sorted == null) {
			Ordering<Distribution> byLocality = new Ordering<Distribution>() {
				@Override
				public int compare(Distribution left, Distribution right) {
					return left.getLocality().compareToIgnoreCase(right.getLocality());
				}
			};

			this.sorted = byLocality.sortedCopy(distributions);
		}

		return sorted;
	}

	public Set<Organisation> getSources() {
		return this.sources;
	}
}
