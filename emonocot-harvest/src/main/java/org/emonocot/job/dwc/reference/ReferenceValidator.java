package org.emonocot.job.dwc.reference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.source.Source;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.TaxonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class ReferenceValidator implements
        ItemProcessor<Reference, Reference>, StepExecutionListener, ChunkListener,
        ItemWriteListener<Reference>{
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(ReferenceValidator.class);

    /**
     *
     */
    private Map<String, Reference> boundReferences = new HashMap<String, Reference>();

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    private ReferenceService referenceService;

    /**
     *
     */
    private StepExecution stepExecution;
    
    /**
     *
     */
  private Source source;
  
  /**
  *
  * @param sourceName Set the id of the source
  */
  public final void setSourceName(String sourceName) {
    source = new Source();
    source.setId(Long.parseLong(sourceName));
  }

    /**
     *
     */
    @Autowired
    public final void setTaxonService(TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     *
     */
    @Autowired
    public final void setReferenceService(ReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    /**
     * @param reference a reference object
     * @throws Exception if something goes wrong
     * @return Reference a reference object
     */
    public final Reference process(final Reference reference)
            throws Exception {
        logger.info("Validating " + reference);

        Reference boundReference = boundReferences.get(reference.getIdentifier());
        if (boundReference == null) {
            Reference persistedReference = referenceService.find(reference.getIdentifier());
            if (persistedReference == null) {
                // We've not seen this reference before
                boundReferences.put(reference.getIdentifier(), reference);
                reference.getSources().add(source);
                reference.setAuthority(source);
                return reference;
            } else {
                // We've seen this reference before, but not in this chunk

                if ((persistedReference.getModified() == null
                    && reference.getModified() == persistedReference.getModified())
                    || persistedReference.getModified().equals(reference.getModified())) {
                    boundReferences.put(persistedReference.getIdentifier(), persistedReference);
                    // Assume the reference hasn't changed, but maybe this taxon
                    // should be associated with it
                    Taxon taxon = reference.getTaxa().iterator().next();
                            
                    if(persistedReference.getTaxa().contains(taxon)) {
                        // do nothing
                    } else {
                        // Add the taxon to the list of taxa
                        persistedReference.getTaxa().add(taxon);
                    }
                    return persistedReference;
                } else {
                    // Assume that this is the first of several times this reference
                    // appears in the result set, and we'll use this version to
                    // overwrite the existing reference
                    reference.setId(persistedReference.getId());
                    boundReferences.put(reference.getIdentifier(), reference);
                    return reference;
                }
            }
        } else {
            // We've already seen this reference within this chunk and we'll
            // update it with this taxon but that's it, assuming that it
            // isn't a more up to date version
            Taxon taxon = reference.getTaxa().iterator().next();
            if (boundReference.getTaxa().contains(taxon)) {
                // do nothing
            } else {
                // Add the taxon to the list of taxa
                boundReference.getTaxa().add(taxon);
            }
            // We've already returned this object once
            return null;
        }
    }

    /**
     * @param newStepExecution Set the step execution
     */
    public final void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
    }

    /**
     * @param newStepExecution Set the step execution
     * @return the exit status
     */
    public final ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }

    /**
     * @param images the list of references written
     */
    public void afterWrite(List<? extends Reference> references) {

    }

    /**
     * @param images the list of references to write
     */
    public void beforeWrite(List<? extends Reference> references) {
        
    }

    /**
     * @param exception the exception thrown
     * @param images the list of references
     */
    public void onWriteError(Exception exception, List<? extends Reference> references) {

    }

    /**
     *
     */
    public void afterChunk() {
    }

    public void beforeChunk() {
        boundReferences.clear();
    }
}
