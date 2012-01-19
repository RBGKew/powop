package org.emonocot.job.checklist;

import java.util.Map;
import java.util.Set;

import org.emonocot.harvest.common.TaxonRelationship;
import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.Taxon;
import org.openarchives.pmh.Record;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.tdwg.voc.TaxonConcept;

/**
 *
 * @author ben
 *
 */
public interface OaiPmhRecordProcessor extends ItemProcessor<Record, Taxon>,
        ChunkListener, StepExecutionListener, ItemWriteListener<Taxon> {

    /**
     *
     * @param taxon Set the taxon object
     * @param taxonConcept Set the taxonConcept object
     */
    void processTaxon(
            final Taxon taxon, final TaxonConcept taxonConcept);

    /**
     *
     * @return the map of inverse relationships
     */
    Map<String, Set<TaxonRelationship>> getInverseRelationships();

    /**
     * @param sourceName set the Source id
     */
    void setSourceName(String sourceName);

    /**
     *
     * @return the current source
     */
    Source getSource();

}
