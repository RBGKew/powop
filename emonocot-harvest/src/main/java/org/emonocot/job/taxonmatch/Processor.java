package org.emonocot.job.taxonmatch;

import java.util.HashSet;
import java.util.Set;

import org.emonocot.api.TaxonService;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Taxon;
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
    private NameParser nameParser;

    /**
     *
     */
    private TaxonService taxonService;

    /**
     * @param nameParser
     *            the nameParser to set
     */
    public void setNameParser(NameParser nameParser) {
        this.nameParser = nameParser;
    }

    /**
     * @param taxonService
     *            the taxonService to set
     */
    public final void setTaxonService(final TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     * @param taxon
     *            Set the taxon
     * @return a result
     * @throws Exception
     *             if there is a problem
     */
    public final Result process(final TaxonDTO taxon) throws Exception {
        logger.warn("Attempting to match " + taxon.getName());
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

        // List<Result> results = matcher.match(parsed);

        Page<org.emonocot.model.taxon.Taxon> page = taxonService.search(
                "\"" + parsed.canonicalName() + "\" "/* + "\""
                        + parsed.getBracketAuthorship() + " "
                        + parsed.getAuthorship()*/, null, null, null, null, null,
                null, null);

        switch (page.getSize()) {
        case 0:
            logger.warn("No matches found for " + taxon.getName());
            result.setStatus(TaxonMatchStatus.NO_MATCH);
            break;
        case 1:
            logger.warn("A single match identified for " + taxon.getName());
            result.setStatus(TaxonMatchStatus.SINGLE_MATCH);
            internal.setIdentifier(page.getRecords().get(0).getIdentifier());
            internal.setName(page.getRecords().get(0).getName());
            // result.setInternal(internal);
            break;
        default:
            Set<org.emonocot.model.taxon.Taxon> matches = new HashSet<org.emonocot.model.taxon.Taxon>();
            for (org.emonocot.model.taxon.Taxon t : page.getRecords()) {
                // logger.warn(t.getName() + " " + t.getIdentifier());
                if (taxon.getName().equals(t.getName())) {
                    matches.add(t);
                }
            }
            result.setPartialMatches(page.getRecords());
            switch (matches.size()) {
            case 0:
                logger.warn("Only partial matches for " + taxon.getName());
                result.setStatus(TaxonMatchStatus.NO_EXACT_MATCH);
                break;
            case 1:
                logger.warn(page.getSize() + " partial matches (1 exact) for "
                        + taxon.getName());
                result.setStatus(TaxonMatchStatus.SINGLE_MATCH);
                Taxon t = matches.iterator().next();
                internal = new TaxonDTO();
                internal.setIdentifier(t.getIdentifier());
                internal.setName(t.getName());
                break;
            default:
                logger.warn(page.getSize() + " partial matches ("
                        + matches.size() + " exact) for " + taxon.getName());
                result.setStatus(TaxonMatchStatus.MULTIPLE_MATCHES);
                result.setPartialMatches(matches);
                break;
            }
        }
        return result;
    }

}
