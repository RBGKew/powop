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
	  Artifical_Hybrid;

	  private static final int ID_BASE = 9000; // reserved upto 9999

	  public boolean isSynonym() {
	    return !(this.equals(Accepted) || this.equals(Doubtful));
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
	    x = x.toLowerCase().trim();
	    if (x.isEmpty()) {
	      return null;
	    }
	    for (TaxonomicStatus term : TaxonomicStatus.values()) {
	      if (term.toString().toLowerCase().equals(x)) {
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
	}