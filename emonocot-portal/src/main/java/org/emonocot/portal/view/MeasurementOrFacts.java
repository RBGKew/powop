package org.emonocot.portal.view;

import java.util.Collection;

import org.emonocot.api.job.WCSPTerm;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.Taxon;
import org.gbif.dwc.terms.Term;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

public class MeasurementOrFacts {

	private Taxon taxon;

	public MeasurementOrFacts(Taxon taxon) {
		this.taxon = taxon;
	}

	public MeasurementOrFact getHabitat() {
		Collection<MeasurementOrFact> habitat = Collections2.filter(taxon.getMeasurementsOrFacts(), new IsType(WCSPTerm.Habitat));
		return habitat.size() == 1 ?
				Iterables.getOnlyElement(habitat) :
				null;
	}

	public String getLifeform() {
		return Joiner.on(" ").join(Collections2.filter(taxon.getMeasurementsOrFacts(), new IsType(WCSPTerm.Lifeform)));
	}

	private class IsType implements Predicate<MeasurementOrFact> {

		private Term type;

		public IsType(Term type) {
			this.type = type;
		}

		@Override
		public boolean apply(MeasurementOrFact input) {
			return input.getMeasurementType() == type;
		}
	}
}
