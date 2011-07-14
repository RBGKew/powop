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
    private String authority;

    /**
     *
     * @param newAuthority Set the authority of the item
     */
    public final void setAuthority(final String newAuthority) {
        this.authority = newAuthority;
    }

    public final Record process(final Record item) {
        // TODO implement validation
        return item;
    }

}
