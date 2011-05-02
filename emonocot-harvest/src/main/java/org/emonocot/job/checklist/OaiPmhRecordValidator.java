package org.emonocot.job.checklist;

import org.emonocot.job.checklist.model.OaiPmhRecord;
import org.springframework.batch.item.ItemProcessor;

/**
 *
 * @author ben
 *
 */
public class OaiPmhRecordValidator implements
        ItemProcessor<OaiPmhRecord, OaiPmhRecord> {

    @Override
    public final OaiPmhRecord process(final OaiPmhRecord item) {
        // TODO implement validation
        return item;
    }

}
