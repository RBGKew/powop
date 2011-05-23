package org.emonocot.job.checklist;

import org.emonocot.model.taxon.Taxon;
import org.openarchives.pmh.Record;
import org.springframework.batch.item.ItemProcessor;

/**
 *
 * @author ben
 *
 */
public class OaiPmhRecordProcessor
    implements ItemProcessor<Record, Taxon> {

    @Override
    public final Taxon process(final Record record) throws Exception {
        Taxon taxon = new Taxon();
        return taxon;
    }

}
