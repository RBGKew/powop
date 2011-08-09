package org.emonocot.job.dwc.taxon;

import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class TaxonValidator implements ItemProcessor<Taxon, Taxon> {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(TaxonValidator.class);

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    @Autowired
    public final void setTaxonService(TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     * @param taxon a taxon object
     * @throws Exception if something goes wrong
     * @return Taxon a taxon object
     */
    public final Taxon process(final Taxon taxon) throws Exception {
        if (taxon.getIdentifier() == null) {
            throw new TaxonProcessingException(taxon + " has no identifier");
        }
        Taxon persistedTaxon = taxonService.find(taxon.getIdentifier());
        if (persistedTaxon == null) {
            throw new TaxonProcessingException(
                    "Cannot find record with identifier "
                    +  taxon.getIdentifier());
        }

        return taxon;
    }

}
