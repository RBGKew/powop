package org.emonocot.job.oaipmh;

import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.Taxon;
import org.openarchives.pmh.Record;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.tdwg.voc.TaxonConcept;

/**
 *
 * @author ben
 *
 */
public interface Processor extends ItemProcessor<Record, Taxon>,
        ChunkListener, StepExecutionListener {

    /**
     *
     * @param taxon Set the taxon object
     * @param taxonConcept Set the taxonConcept object
     */
    void processTaxon(
            final Taxon taxon, final TaxonConcept taxonConcept);

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
