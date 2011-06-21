package org.emonocot.job.dwc.taxon;

import org.emonocot.model.taxon.Taxon;
import org.springframework.batch.item.ItemProcessor;

/**
 *
 * @author ben
 *
 */
public class TaxonValidator implements ItemProcessor<Taxon, Taxon> {

    @Override
    public final Taxon process(final Taxon taxon) throws Exception {
        // TODO Auto-generated method stub
        return taxon;
    }

}
