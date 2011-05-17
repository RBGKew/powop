package org.emonocot.checklist.view.oai.rdf;

import org.emonocot.checklist.model.IdentifiableEntity;
import org.openarchives.pmh.Metadata;
import org.tdwg.voc.TaxonConcept;

/**
 *
 * @author ben
 *
 */
public class ListRecordsView extends
        org.emonocot.checklist.view.oai.ListRecordsView {

    @Override
    public final void constructMetadata(final Metadata metadata,
            final IdentifiableEntity identifiableEntity) {
        metadata.setTaxonConcept(getMapper().map(identifiableEntity,
                TaxonConcept.class));
    }

}
