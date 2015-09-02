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
package org.emonocot.job.taxonmatch;

import java.util.ArrayList;
import java.util.List;

import org.emonocot.api.match.Match;
import org.emonocot.api.match.MatchStatus;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.model.Taxon;
import org.gbif.ecat.parser.UnparsableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A way of chaining a set of matchers, such that the first list of matches with an exact match will be returned.  If there are none, the first list of partial matches is returned
 *
 * @author jk00kg
 */
public class CompositeTaxonMatcher implements TaxonMatcher {

	/**
	 *
	 */
	private Logger logger = LoggerFactory.getLogger(CompositeTaxonMatcher.class);

	/**
	 * An array organised in order of preference for matching
	 */
	private TaxonMatcher[] matchers;

	/**
	 * @param matchers the matchers to set
	 */
	public final void setMatchers(TaxonMatcher[] matchers) {
		this.matchers = matchers;
	}

	/*
	 * Returns one or more match of the same status
	 * @see
	 * org.emonocot.api.match.TaxonMatcher#match(org.gbif.ecat.model.ParsedName
	 * )
	 */
	public final List<Match<Taxon>> match(String name) throws UnparsableException {
		List<Match<Taxon>> matches = new ArrayList<Match<Taxon>>();
		List<Match<Taxon>> partialMatches = new ArrayList<Match<Taxon>>();

		int i = -1;
		while(++i < matchers.length && matches.size() < 1){
			TaxonMatcher matcher = matchers[i];
			List<Match<Taxon>> results = matcher.match(name);
			for (Match<Taxon> m : results) {
				if(MatchStatus.EXACT.equals(m.getStatus())){
					matches = results;
				} else if (partialMatches.size() < 1) {
					partialMatches = results;
				}
			}
		}

		if (matches.size() > 0){
			return matches;
		} else {
			return partialMatches;
		}
	}

}
