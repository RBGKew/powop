package org.emonocot.job.checklist;

import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
import org.hibernate.engine.Status;
import org.openarchives.pmh.Record;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class OaiPmhRecordProcessor
    implements ItemProcessor<Record, Taxon> {

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     * @param taxonService Set the taxon service
     */
    @Autowired
    public final void setTaxonService(final TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    @Override
    public final Taxon process(final Record record) throws Exception {
        Taxon taxon = taxonService.find(record.getHeader().getIdentifier()
                .toString());

        if (taxon == null) {
            // We don't have a record of this taxon yet
            if (record.getHeader().getStatus() != null
                    && record.getHeader().getStatus().equals(Status.DELETED)) {
                // It was created and then deleted in between harvesting - so
                // ignore.
                return null;
            } else {
               // Create a new taxon
               taxon = new Taxon();
            }
        } else {
         // We don't have a record of this taxon yet
            if (record.getHeader().getStatus() != null
                    && record.getHeader().getStatus().equals(Status.DELETED)) {
                // We have a record of it and now we need to delete it
                taxon.setDeleted(true);
            } else {
                // We have a record of the taxon and now we should update it
            }
        }


        return taxon;
    }

}
