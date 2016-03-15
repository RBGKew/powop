/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.api.job;

import org.gbif.dwc.terms.Term;


/**
 * @author jc20kg
 * adding in some of the terms not previously included in 
 */
public enum ExtendedAcTerm implements Term {
	WorldRegion,
	CountryCode,
	CountryName,
	ProvinceState,
	Sublocation,
	PixelXDimension,
	PixelYDimension,
	Rating;

	public static final String NS = "http://rs.tdwg.org/ac/terms/";
	public static final String PREFIX = "ac";
	static final String[] PREFIXES = {NS, PREFIX + ":"};

	public final String normQName;
	public final String[] normAlts;

	ExtendedAcTerm(String... alternatives) {
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

	@Deprecated
	public String qualifiedNormalisedName() {
		return normQName;
	}

	@Override
	public String simpleName() {
		return name();
	}

	@Deprecated
	public String[] simpleNormalisedAlternativeNames() {
		return normAlts;
	}

	@Deprecated
	public String simpleNormalisedName() {
		return TermFactory.normaliseTerm(simpleName());
	}

	@Override
	public String toString() {
		return PREFIX + ":" + name();
	}
}
