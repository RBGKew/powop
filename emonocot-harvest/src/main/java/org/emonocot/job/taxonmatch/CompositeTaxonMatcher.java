/**
 *
 */
package org.emonocot.job.taxonmatch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.emonocot.api.TaxonService;
import org.emonocot.api.match.Match;
import org.emonocot.api.match.MatchStatus;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Taxon;
import org.gbif.ecat.model.ParsedName;
import org.gbif.ecat.parser.NameParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
    public final List<Match<Taxon>> match(final ParsedName<String> parsed) {
		List<Match<Taxon>> matches = new ArrayList<Match<Taxon>>();
		List<Match<Taxon>> partialMatches = new ArrayList<Match<Taxon>>();
		
    	int i = -1;
    	while(++i < matchers.length && matches.size() < 1){
    		TaxonMatcher matcher = matchers[i];
    		List<Match<Taxon>> results = matcher.match(parsed);
            for (Match<Taxon> m : results) {
                if(MatchStatus.EXACT.equals(m.getStatus())){
                	matches = results;
                }else if (partialMatches.size() < 1) {
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
