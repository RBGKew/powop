package org.emonocot.api.job;

import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.TermFactory;

public enum WCSPTerm implements ConceptTerm {
	Habitat, Lifeform;

	public static final String NS = "http://kew.org/wcs/terms/";
	public static final String PREFIX = "wcsp";
	static final String[] PREFIXES = { NS, PREFIX + ":" };

	public final String normQName;
	public final String[] normAlts;

	WCSPTerm(String... alternatives) {
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
