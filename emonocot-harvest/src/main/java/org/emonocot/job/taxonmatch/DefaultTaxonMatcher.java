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
import org.gbif.ecat.voc.Rank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jk00kg
 */
public class DefaultTaxonMatcher implements TaxonMatcher {

    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(DefaultTaxonMatcher.class);

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
        StringBuilder stringBuilder = new StringBuilder();
        if (parsed.getSpecificEpithet() == null) {
            stringBuilder.append("label:" + parsed.getGenusOrAbove());
            if(parsed.getAuthorship() != null) {
                stringBuilder.append(" AND authorship:"
                        + parsed.getAuthorship());
            }
        } else {
            stringBuilder.append("genus:" + parsed.getGenusOrAbove());
            if (parsed.getSpecificEpithet() != null) {
                stringBuilder.append(" AND specificEpithet:"
                        + parsed.getSpecificEpithet());
            }
            if (parsed.getInfraGeneric() != null) {
                stringBuilder.append(" AND infraGenericEpithet:"
                        + parsed.getInfraGeneric());
            }
            if (parsed.getInfraSpecificEpithet() != null) {
                stringBuilder.append(" AND infraSpecificEpithet:"
                        + parsed.getInfraSpecificEpithet());
            }
            if (parsed.getRank() != null) {
                if (parsed.getRank().equals(Rank.SPECIES)) {
                    stringBuilder.append(" AND rank:species");
                } else {

                }

            }
            if(parsed.getAuthorship() != null) {
                stringBuilder.append(" AND authorship:"
                        + parsed.getAuthorship());
            }
        }
        // String searchTerm = parsed.canonicalName();// .buildName(true, true,
        // false, false, false, false, true, false, false, false);
        String searchTerm = stringBuilder.toString();
        logger.debug("Attempting to match " + searchTerm);
        List<Match<Taxon>> matches = new ArrayList<Match<Taxon>>();
        Page<Taxon> page = taxonService.search(searchTerm, null, null, null, null, null, null, null);

        switch (page.getRecords().size()) {
        case 0:
        	if(parsed.getBracketAuthorship() != null){
        		logger.info("removing bracket authorship " + parsed.getAuthorship());
        		parsed.setBracketAuthorship(null);
        		logger.info("'null' bracket authorship is " + parsed.getAuthorship());
        		matches = match(parsed);
        	} else if (parsed.getAuthorship() != null) {
        		logger.info("removing authorship " + parsed.getAuthorship());
        		parsed.setAuthorship(null);
        		logger.info("'null' authorship is " + parsed.getAuthorship());
        		matches = match(parsed);
        	}
            break;
        case 1:
            Match<Taxon> single = new Match<Taxon>();
            single.setInternal(page.getRecords().get(0));
            String internalName = (new NameParser().parseToCanonical(single.getInternal().getName()));
            if (searchTerm.equals(internalName)) {
                single.setStatus(MatchStatus.EXACT);
            } else {
                single.setStatus(MatchStatus.PARTIAL);
            }
            matches.add(single);
            break;
        default:
            Set<Match<Taxon>> exactMatches = new HashSet<Match<Taxon>>();
            for (Taxon eTaxon : page.getRecords()) {
                logger.debug(eTaxon.getName() + " " + eTaxon.getIdentifier());
                Match<Taxon> m = new Match<Taxon>();
                m.setInternal(eTaxon);
                matches.add(m);
                String name = (new NameParser().parseToCanonical(eTaxon.getName()));
                if (searchTerm.equals(name)) {
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
                break;
            }
        }
        return matches;
    }

}
