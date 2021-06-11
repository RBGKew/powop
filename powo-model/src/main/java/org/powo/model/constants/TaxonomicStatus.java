package org.powo.model.constants;

import org.gbif.ecat.voc.KnownTerm;
import org.gbif.ecat.voc.TermType;

public enum TaxonomicStatus implements KnownTerm {
	Accepted,
	Synonym,
	Heterotypic_Synonym,
	Homotypic_Synonym,
	Proparte_Synonym,
	Misapplied,
	IntermediateRankSynonym,
	// used for unkown child taxa referred to via spec, ssp, ...
	DeterminationSynonym,
	/**
	 * Treated as accepted, but doubtful whether this is correct.
	 */
	Doubtful,
	Artificial_Hybrid,
	Unplaced,
	/*
	 * Treated as accepted, as the plant/fungi community has not yet
	 * come to an agreement on taxonomy. Some authorities consider
	 * it accepted, some do not (yet).
	 */
	PartiallyAccepted;

	private static final int ID_BASE = 9000; // reserved upto 9999

	public boolean isSynonym() {
		return !(this.equals(Accepted) || this.equals(Doubtful) || this.equals(PartiallyAccepted));
	}

	@Override
	public int termID() {
		return ID_BASE + ordinal();
	}

	@Override
	public TermType type() {
		return TermType.TAXONOMIC_STATUS;
	}

	@Deprecated
	public static TaxonomicStatus valueOf(int termID) {
		return valueOfTermID(termID);
	}

	public static TaxonomicStatus valueOfTermID(Integer termID) {
		for (TaxonomicStatus term : TaxonomicStatus.values()) {
			if (termID != null && term.termID() == termID) {
				return term;
			}
		}
		return null;
	}

	public static TaxonomicStatus fromString(String x) {
		if (x == null) {
			return null;
		}
		x = normalise(x);
		if (x.isEmpty()) {
			return null;
		}
		for (TaxonomicStatus term : TaxonomicStatus.values()) {
			if (normalise(term.toString()).equals(x)) {
				return term;
			}
		}
		if (x.startsWith("acc")) {
			return Accepted;
		}
		if (x.startsWith("syn")) {
			return Synonym;
		}
		return null;
	}

	private static String normalise(String str) {
		return str.toLowerCase().replaceAll(" |_", "");
	}
}