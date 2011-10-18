package org.emonocot.job.taxonmatch;

import java.util.HashSet;
import java.util.Set;

import org.emonocot.api.TaxonService;
import org.emonocot.model.pager.Page;
import org.springframework.batch.item.ItemProcessor;

/**
 *
 * @author ben
 *
 */
public class Processor implements ItemProcessor<Taxon, Result> {
    /**
     *
     */
    private TaxonService taxonService;

    /**
     * @param taxonService the taxonService to set
     */
    public final void setTaxonService(final TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     * @param taxon Set the taxon
     * @return a result
     * @throws Exception if there is a problem
     */
    public final Result process(final Taxon taxon) throws Exception {
        Page<org.emonocot.model.taxon.Taxon> page = taxonService.search("\""
                + taxon.getName() + "\"", null, null, null, null, null, null);     
        Result result = new Result();
        result.setOriginalIdentifier(taxon.getIdentifier());
        result.setName(taxon.getName());
        switch(page.getSize()) {
        case 0:
            result.setStatus(TaxonMatchStatus.NO_MATCH);
            break;
        case 1:
            result.setStatus(TaxonMatchStatus.SINGLE_MATCH);
            result.setIdentifier(page.getRecords().get(0).getIdentifier());
            break;
        default:
            Set<org.emonocot.model.taxon.Taxon> matches = new HashSet<org.emonocot.model.taxon.Taxon>();
            for (org.emonocot.model.taxon.Taxon t : page.getRecords()) {
                if (t.getName().equals(taxon.getName())) {
                    matches.add(t);
                }
            }
            if (matches.size() == 0) {
                result.setStatus(TaxonMatchStatus.NO_EXACT_MATCH);
                result.setPartialMatches(page.getRecords());
            } else if (matches.size() == 1) {
                result.setStatus(TaxonMatchStatus.SINGLE_MATCH);
                result.setIdentifier(matches.iterator().next().getIdentifier());
            } else {
                result.setStatus(TaxonMatchStatus.NO_EXACT_MATCH);
                result.setPartialMatches(matches);
            }
        }
        return result;
    }

}
