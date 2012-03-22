/**
 *
 */
package org.emonocot.job.taxonmatch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.emonocot.api.TaxonService;
import org.emonocot.api.taxonmatch.Match;
import org.emonocot.api.taxonmatch.MatchStatus;
import org.emonocot.api.taxonmatch.TaxonDTO;
import org.emonocot.api.taxonmatch.TaxonMatcher;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Taxon;
import org.gbif.ecat.model.ParsedName;
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
     * org.emonocot.api.taxonmatch.TaxonMatcher#match(org.gbif.ecat.model.ParsedName
     * )
     */
    public final List<Match> match(final ParsedName<String> parsed) {
        logger.debug("Attempting to match " + parsed.canonicalName());
        List<Match> matches = new ArrayList<Match>();
        Page<Taxon> page = taxonService.search("\""
                + parsed.canonicalName() + "\" ", null, null, null, null,
                null, null, null);

        switch (page.getRecords().size()) {
        case 0:
            break;
        case 1:
            Match single = new Match();
            single.setInternal(convert(page.getRecords().get(0)));
            if (parsed.canonicalName().equals(single.getInternal().getName())) {
                single.setStatus(MatchStatus.EXACT);
            } else {
                single.setStatus(MatchStatus.PARTIAL);
            }
            matches.add(single);
            break;
        default:
            Set<Match> exactMatches = new HashSet<Match>();
            for (Taxon eTaxon : page.getRecords()) {
                logger.debug(eTaxon.getName() + " " + eTaxon.getIdentifier());
                Match m = new Match();
                m.setInternal(convert(eTaxon));
                matches.add(m);
                if (parsed.canonicalName().equals(eTaxon.getName())) {
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
                for (Match m : exactMatches) {
                    logger.debug(m.getInternal().getName() + " exact "
                            + m.getInternal().getIdentifier());
                }
                break;
            }
        }

        return matches;
    }

    /**
     * @param taxon
     *            an emonocot Taxon object
     * @return a simple TaxonDTO with name and identifier
     */
    private TaxonDTO convert(final Taxon taxon) {
        TaxonDTO dto = new TaxonDTO();
        dto.setIdentifier(taxon.getIdentifier());
        dto.setName(taxon.getName());
        return dto;
    }

    /**
     * @param records
     *            a list of emonocot Taxon objects
     * @return a list of simple TaxonDTOs with name and identifier
     */
    private List<TaxonDTO> convert(final List<Taxon> records) {

        List<TaxonDTO> dtos = new ArrayList<TaxonDTO>();
        for (Taxon eTaxon : records) {
            TaxonDTO dto = new TaxonDTO();
            dto.setIdentifier(eTaxon.getIdentifier());
            dto.setName(eTaxon.getName());
            dtos.add(dto);
        }
        return dtos;
    }

}
