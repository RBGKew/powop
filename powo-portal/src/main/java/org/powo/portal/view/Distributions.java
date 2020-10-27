package org.powo.portal.view;

import static java.util.stream.Collectors.toList;
import static org.gbif.ecat.voc.EstablishmentMeans.Introduced;
import static org.gbif.ecat.voc.EstablishmentMeans.Native;
import static org.gbif.ecat.voc.OccurrenceStatus.Doubtful;
import static org.gbif.ecat.voc.OccurrenceStatus.Absent;
import static org.gbif.ecat.voc.ThreatStatus.Extinct;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.powo.model.Distribution;
import org.powo.model.Taxon;
import org.powo.model.registry.Organisation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;

@JsonInclude(Include.NON_EMPTY)
public class Distributions {

	private Set<Distribution> distributions;

	@Getter(lazy = true) private final List<Distribution> natives = filterBy(d -> Native.equals(d.getEstablishment()));
	@Getter(lazy = true) private final List<Distribution> introduced = filterBy(d -> Introduced.equals(d.getEstablishment()));
	@Getter(lazy = true) private final List<Distribution> doubtful = filterBy(d -> Doubtful.equals(d.getOccurrenceStatus()));
	@Getter(lazy = true) private final List<Distribution> extinct = filterBy(d -> Extinct.equals(d.getThreatStatus()));
	@Getter(lazy = true) private final List<Distribution> absent = filterBy(d -> Absent.equals(d.getOccurrenceStatus()));

	@Getter
	@JsonIgnore
	private final Set<Organisation> sources;

	private Comparator<Distribution> byLocality = (left, right) -> left.getLocality().compareToIgnoreCase(right.getLocality());

	public Distributions(Taxon taxon) {
		this.distributions = taxon.getDistribution();
		this.sources = new HashSet<>();
		for(Distribution d : distributions) {
			sources.add(d.getAuthority());
		}
	}

	private List<Distribution> filterBy(Predicate<Distribution> predicate) {
		return distributions.stream()
				.filter(predicate)
				.sorted(byLocality)
				.collect(toList());
	}
}
