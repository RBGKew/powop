package org.emonocot.job.dwc;

import org.emonocot.model.taxon.Taxon;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class TaxonFieldSetMapper implements FieldSetMapper<Taxon> {

    @Override
    public final Taxon mapFieldSet(final FieldSet fieldSet)
            throws BindException {
        Taxon taxon = new Taxon();
        
        return taxon;
    }

}
