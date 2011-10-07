package org.emonocot.job.checklist;

import org.openarchives.pmh.Record;
import org.springframework.batch.item.ItemProcessor;

/**
 *
 * @author ben
 *
 */
public class OaiPmhRecordValidator implements
        ItemProcessor<Record, Record> {
    /**
     *
     */
    private String Source;

    /**
     *
     * @param newSource Set the Source of the item
     */
    public final void setSource(final String newSource) {
        this.Source = newSource;
    }

    public final Record process(final Record item) {
        // TODO implement validation
        return item;
    }

}
