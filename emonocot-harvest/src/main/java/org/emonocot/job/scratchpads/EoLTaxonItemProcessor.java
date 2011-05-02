package org.emonocot.job.scratchpads;


import org.emonocot.job.scratchpads.model.EoLTaxonItem;
import org.emonocot.model.taxon.Taxon;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class EoLTaxonItemProcessor implements
        ItemProcessor<EoLTaxonItem, Taxon> {

    /**
     *
     */
    private Converter<EoLTaxonItem, Taxon> taxonItemConverter;

    /**
     *
     * @param taxonItemConverter Set the taxon item converter to use.
     */
    @Autowired
    public final void setTaxonItemConverter(
            final Converter<EoLTaxonItem, Taxon> taxonItemConverter) {
        this.taxonItemConverter = taxonItemConverter;
    }

    /**
     * @param input The eol taxon item to process
     * @return A Taxon object
     */
    public final Taxon process(final EoLTaxonItem input) {
        return taxonItemConverter.convert(input);
    }
}
