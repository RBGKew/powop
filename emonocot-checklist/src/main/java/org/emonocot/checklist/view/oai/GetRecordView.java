package org.emonocot.checklist.view.oai;

import java.util.Map;

import org.emonocot.checklist.controller.oai.AbstractOaiPmhController;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.IdentifiableEntity;
import org.openarchives.pmh.GetRecord;
import org.openarchives.pmh.Header;
import org.openarchives.pmh.Metadata;
import org.openarchives.pmh.OAIPMH;
import org.openarchives.pmh.Record;

/**
 *
 * @author ben
 *
 */
public abstract class GetRecordView extends AbstractOaiPmhResponseView {

    /**
     * @param oaiPmh Set the oai pmh response to use
     * @param model Set the model map to merge in
     */
    protected final void constructResponse(final OAIPMH oaiPmh,
            final Map<String, Object> model) {
        GetRecord getRecord = new GetRecord();
        ChangeEvent<IdentifiableEntity> changeEvent
            = (ChangeEvent<IdentifiableEntity>)
            model.get(AbstractOaiPmhController.OBJECT_KEY);
        Record record = new Record();
        record.setHeader(getMapper().map(changeEvent, Header.class));
        if (!changeEvent.getType().equals(ChangeType.DELETE)) {
            Metadata metadata = new Metadata();
            constructMetadata(metadata,
                    (IdentifiableEntity) changeEvent.getObject());
            record.setMetadata(metadata);
        }

        getRecord.setRecord(record);
        oaiPmh.setGetRecord(getRecord);
    }

    /**
     *
     * @param metadata
     *            Set the metadata element to use
     * @param identifiableEntity
     *            Set the identifiable entity to add to the metadata element
     */
    public abstract void constructMetadata(Metadata metadata,
            IdentifiableEntity identifiableEntity);
}