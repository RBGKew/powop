package org.emonocot.job.scratchpads;

import org.emonocot.job.scratchpads.model.EoLTaxonItem;
import org.emonocot.service.TaxonService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class EoLTaxonItemValidator implements
        ItemProcessor<EoLTaxonItem, EoLTaxonItem> {

    /**
     *
     */
    private String authority;

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     * @param taxonService Set the taxon service to use
     */
    @Autowired
    public final void setTaxonService(final TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     * @param item
     *            The item to process
     * @return The unmodified eol taxon item if the item provided is valid, or
     *         null if it is invalid
     */
    public final EoLTaxonItem process(final EoLTaxonItem item) {
        if (taxonService.verify(item.getIdentifer(),
                item.getScientificName())) {
            return item;
        } else {
            return null;
        }
    }

    /**
     *
     * @param newAuthority Set the authority from which this item was harvested
     */
    public final void setAuthority(final String newAuthority) {
        this.authority = newAuthority;
    }
}
