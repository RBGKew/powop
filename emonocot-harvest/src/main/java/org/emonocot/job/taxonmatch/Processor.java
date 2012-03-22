package org.emonocot.job.taxonmatch;

import java.util.List;

import org.emonocot.api.taxonmatch.Match;
import org.emonocot.api.taxonmatch.MatchStatus;
import org.emonocot.api.taxonmatch.TaxonDTO;
import org.emonocot.api.taxonmatch.TaxonMatcher;
import org.gbif.ecat.model.ParsedName;
import org.gbif.ecat.parser.NameParser;
import org.gbif.ecat.parser.UnparsableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author ben
 */
public class Processor implements ItemProcessor<TaxonDTO, Result> {

    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(Processor.class);

    /**
     *
     */
    private TaxonMatcher taxonMatcher;

    /**
     *
     */
    private NameParser nameParser;

    /**
     * @param newTaxonMatcher
     *            the matcher to set
     */
    public void setTaxonMatcher(TaxonMatcher newTaxonMatcher) {
        this.taxonMatcher = newTaxonMatcher;
    }

    /**
     * @param newNameParser
     *            the nameParser to set
     */
    public void setNameParser(NameParser newNameParser) {
        this.nameParser = newNameParser;
    }

    /**
     * @param taxon
     *            Set the taxon
     * @return a result
     * @throws Exception
     *             if there is a problem
     */
    public final Result process(final TaxonDTO taxon) throws Exception {
        logger.debug("Attempting to match " + taxon.getName());
        Result result = new Result();
        TaxonDTO internal = new TaxonDTO();
        result.setExternal(taxon);
        result.setInternal(internal);
        result.setName(taxon.getName());

        ParsedName<String> parsed = null;

        try {
            parsed = nameParser.parse(taxon.getName());
            if (parsed == null) {
                throw new UnparsableException(null, taxon.getName());
            }
        } catch (UnparsableException e) {
            result.setStatus(TaxonMatchStatus.CANNOT_PARSE);
            return result;
        }

        List<Match> matches = taxonMatcher.match(parsed);

        switch (matches.size()) {
        case 0:
            logger.debug("No matches found for " + taxon.getName());
            result.setStatus(TaxonMatchStatus.NO_MATCH);
            break;
        case 1:
            logger.debug("A single match identified for " + taxon.getName());
            Match single = matches.get(0);
            if(single.getStatus().equals(MatchStatus.EXACT)){
                result.setStatus(TaxonMatchStatus.SINGLE_MATCH);
            } else {
                result.setStatus(TaxonMatchStatus.NO_EXACT_MATCH);
            }
            result.setInternal(single.getInternal());
            break;
        default:
            logger.debug(matches.size() + " matches for "
                    + taxon.getName());
            int exact = 0;
            for(Match match : matches) {
                if(match.getStatus().equals(MatchStatus.EXACT)) {
                    exact++;
                    result.setInternal(match.getInternal());
                }
            }
            if(exact > 1) {
                result.setStatus(TaxonMatchStatus.MULTIPLE_MATCHES);
                result.setInternal(internal);
            } else {
                result.setStatus(TaxonMatchStatus.NO_EXACT_MATCH);
            }
            break;
        }
        return result;
    }

}
