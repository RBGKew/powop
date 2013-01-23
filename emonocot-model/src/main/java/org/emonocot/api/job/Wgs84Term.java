package org.emonocot.api.job;

import org.gbif.dwc.terms.ConceptTerm;

public enum Wgs84Term implements ConceptTerm {
	latitude,
	longitude;

	  public static final String NS = "http://www.w3.org/2003/01/geo/wgs84_pos#";
	  public static final String PREFIX = "wgs84";
	  static final String[] PREFIXES = {NS, PREFIX + ":"};

	  public final String normQName;
	  public final String[] normAlts;

	  Wgs84Term(String... alternatives) {
	    normQName = TermFactory.normaliseTerm(qualifiedName());
	    for (int i = 0; i < alternatives.length; i++) {
	      alternatives[i] = TermFactory.normaliseTerm(alternatives[i]);
	    }
	    normAlts = alternatives;
	  }

	  @Override
	  public String qualifiedName() {
	    return NS + name();
	  }

	  @Override
	  public String qualifiedNormalisedName() {
	    return normQName;
	  }

	  @Override
	  public String simpleName() {
	    return name();
	  }

	  @Override
	  public String[] simpleNormalisedAlternativeNames() {
	    return normAlts;
	  }

	  @Override
	  public String simpleNormalisedName() {
	    return TermFactory.normaliseTerm(simpleName());
	  }

	  @Override
	  public String toString() {
	    return PREFIX + ":" + name();
	  }
	}
