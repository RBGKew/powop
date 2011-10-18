package org.emonocot.job.scratchpads;

import org.emonocot.job.scratchpads.model.EoLTaxonItem;
import org.emonocot.api.TaxonService;
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
    private String Source;

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
        return item;
    }

    /**
     *
     * @param newSource Set the Source from which this item was harvested
     */
    public final void setSource(final String newSource) {
        this.Source = newSource;
    }
}
