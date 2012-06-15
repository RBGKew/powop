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
 * @author jk00kg
 */
public class ExampleTaxonMatcher implements TaxonMatcher {

    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(ExampleTaxonMatcher.class);

    /**
     *
     */
    @Autowired
    private TaxonService taxonService;

    /**
     * @param newTaxonService
     *            the taxonService to set
     */
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    /*
     * Returns one or more match of the same status
     * @see
     * org.emonocot.api.match.TaxonMatcher#match(org.gbif.ecat.model.ParsedName
     * )
     */
    public final List<Match<Taxon>> match(final ParsedName<String> parsed) {
        List<Match<Taxon>> matches = new ArrayList<Match<Taxon>>();
        Taxon emonocotTaxon = new Taxon();
        emonocotTaxon.setName(parsed.buildName(true, true, false, false, false,
                false, true, false, false, false));
        if (parsed.getAuthorship() != null) {
        	emonocotTaxon.setAuthorship(parsed.getAuthorship());
        }
        if (parsed.getBracketAuthorship() != null) {
        	emonocotTaxon.setBasionymAuthorship(parsed.getBracketAuthorship());
        }
        logger.debug("Attempting to match " + emonocotTaxon.getName());

        Page<Taxon> page = taxonService.searchByExample(emonocotTaxon, true, true);

        switch (page.getRecords().size()) {
        case 0:
        	if(parsed.getBracketAuthorship() != null){
        		parsed.setBracketAuthorship(null);
        		matches = match(parsed);
        	} else if (parsed.getAuthorship() != null) {
        		parsed.setAuthorship(null);
        		matches = match(parsed);
        	}
        	for (Match<Taxon> match : matches) {
				match.setStatus(MatchStatus.PARTIAL);
			}
            break;
        case 1:
            Match<Taxon> single = new Match<Taxon>();
            single.setInternal(page.getRecords().get(0));
            String internalName = (new NameParser().parseToCanonical(single.getInternal().getName()));
            if (emonocotTaxon.getName().equals(internalName)) {
                single.setStatus(MatchStatus.EXACT);
            } else {
                single.setStatus(MatchStatus.PARTIAL);
            }
            matches.add(single);
            break;
        default:
            Set<Match<Taxon>> exactMatches = new HashSet<Match<Taxon>>();
            for (Taxon taxon : page.getRecords()) {
                logger.debug(taxon.getName() + " " + taxon.getIdentifier());
                Match<Taxon> m = new Match<Taxon>();
                m.setInternal(taxon);
                matches.add(m);
                String name = (new NameParser().parseToCanonical(taxon.getName()));
                if (emonocotTaxon.getName().equals(name)) {
                    m.setStatus(MatchStatus.EXACT);
                    exactMatches.add(m);
                } else {
                    m.setStatus(MatchStatus.PARTIAL);
                }
            }
            switch (exactMatches.size()) {
            case 0:
                break;
            case 1:
                matches.retainAll(exactMatches);
                break;
            default:
                logger.debug(exactMatches.size() + " exact matches:");
                for (Match<Taxon> m : exactMatches) {
                    logger.debug(m.getInternal().getName() + " exact "
                            + m.getInternal().getIdentifier());
                }
                break;
            }
        }

        return matches;
    }
}
