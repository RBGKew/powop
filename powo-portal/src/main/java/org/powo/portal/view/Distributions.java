package org.powo.portal.view;

import static org.gbif.ecat.voc.EstablishmentMeans.Introduced;
import static org.gbif.ecat.voc.EstablishmentMeans.Native;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.powo.model.Distribution;
import org.powo.model.Taxon;
import org.powo.model.registry.Organisation;
import org.gbif.ecat.voc.EstablishmentMeans;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;

public class Distributions {

	private Set<Distribution> distributions;
	private List<Distribution> nativ;
	private List<Distribution> introduced;
	private Set<Organisation> sources;

	private Ordering<Distribution> byLocality = new Ordering<Distribution>() {
		@Override
		public int compare(Distribution left, Distribution right) {
			return left.getLocality().compareToIgnoreCase(right.getLocality());
		}
	};

	private class FilterByEstablishment implements Predicate<Distribution> {
		private EstablishmentMeans establishment;

		public FilterByEstablishment(EstablishmentMeans em) {
			this.establishment = em;
		}

		@Override
		public boolean apply(Distribution input) {
			return nativeOrIntroduced(input.getEstablishmentMeans()).equals(establishment);
		}
	}

	public Distributions(Taxon taxon) {
		this.distributions = taxon.getDistribution();
		this.sources = new HashSet<>();
		for(Distribution d : distributions) {
			sources.add(d.getAuthority());
		}
	}

	public List<Distribution> getNative() {
		if(nativ == null) {
			nativ = byLocality.sortedCopy(
					Collections2.filter(distributions, new FilterByEstablishment(Native)));
		}

		return nativ;
	}

	public List<Distribution> getIntroduced() {
		if(introduced == null) {
			introduced = byLocality.sortedCopy(
					Collections2.filter(distributions, new FilterByEstablishment(Introduced)));
		}

		return introduced;
	}

	public Set<Organisation> getSources() {
		return this.sources;
	}

	public static EstablishmentMeans nativeOrIntroduced(EstablishmentMeans em) {
		if (em == null) {
			return Native;
		} else {
			switch (em) {
			case Introduced:
			case Invasive:
			case Managed:
			case Naturalised:
				return Introduced;
			case Uncertain:
			case Native:
			default:
				return Native;
			}
		}
	}
}
