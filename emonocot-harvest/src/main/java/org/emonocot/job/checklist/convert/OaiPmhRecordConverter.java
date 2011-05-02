package org.emonocot.job.checklist.convert;

import org.emonocot.job.checklist.model.OaiPmhRecord;
import org.emonocot.model.taxon.Taxon;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class OaiPmhRecordConverter implements Converter<OaiPmhRecord, Taxon> {

    @Override
    public final Taxon convert(final OaiPmhRecord item) {
        // TODO Auto-generated method stub
        return new Taxon();
    }

}
