package org.emonocot.checklist.view.oai;

import java.util.Map;

import org.emonocot.checklist.controller.oai.AbstractOaiPmhController;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.IdentifiableEntity;
import org.emonocot.checklist.persistence.pager.Page;
import org.openarchives.pmh.Header;
import org.openarchives.pmh.ListRecords;
import org.openarchives.pmh.Metadata;
import org.openarchives.pmh.OAIPMH;
import org.openarchives.pmh.Record;
import org.openarchives.pmh.ResumptionToken;

/**
 *
 * @author ben
 *
 */
public abstract class ListRecordsView extends AbstractOaiPmhResponseView {

    /**
     * @param oaiPmh the OAIPMH object to add the list of records to
     * @param model the model map to get the records from
     */
    protected final void constructResponse(final OAIPMH oaiPmh,
            final Map<String, Object> model) {

        ListRecords listRecords = new ListRecords();

        for (ChangeEvent changeEvent : ((Page<ChangeEvent>) model
                .get(AbstractOaiPmhController.OBJECT_KEY)).getRecords()) {
            Record record = new Record();
            record.setHeader(super.getMapper().map(changeEvent, Header.class));
            if (!changeEvent.getType().equals(ChangeType.DELETE)) {
                Metadata metadata = new Metadata();
                constructMetadata(metadata,
                        (IdentifiableEntity) changeEvent.getObject());
                record.setMetadata(metadata);
            }
            listRecords.getRecord().add(record);
        }

        if (model.containsKey(AbstractOaiPmhController.RESUMPTION_TOKEN_KEY)) {
            listRecords.setResumptionToken((ResumptionToken) model
                    .get(AbstractOaiPmhController.RESUMPTION_TOKEN_KEY));
        }

        oaiPmh.setListRecords(listRecords);
    }

    /**
     *
     * @param metadata A metadata object to insert a record into
     * @param identifiableEntity An identifiable entity to map
     */
    public abstract void constructMetadata(Metadata metadata,
            IdentifiableEntity identifiableEntity);
}