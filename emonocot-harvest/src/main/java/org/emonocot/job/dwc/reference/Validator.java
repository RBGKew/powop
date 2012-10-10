package org.emonocot.job.dwc.reference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.emonocot.api.ReferenceService;
import org.emonocot.job.dwc.DarwinCoreValidator;
import org.emonocot.model.Annotation;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class Validator extends DarwinCoreValidator<Reference>
        implements ChunkListener, ItemWriteListener<Reference> {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(Validator.class);

    /**
     *
     */
    private Map<String, Reference> boundReferences = new HashMap<String, Reference>();

    /**
     *
     */
    private ReferenceService referenceService;

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
        
        super.checkTaxon(RecordType.Reference, reference, reference.getTaxa().iterator().next());

        Reference boundReference = lookupBoundReference(reference);
        if (boundReference == null) {
            Reference persistedReference = retrieveBoundReference(reference);
            if (persistedReference == null) {
                // We've not seen this reference before
                reference.setIdentifier(UUID.randomUUID().toString());
                bindReference(reference);
                for (Taxon t : reference.getTaxa()) {
                    t.getReferences().add(reference);
                }
                reference.getSources().add(getSource());
                reference.setAuthority(getSource());
                Annotation annotation = createAnnotation(reference,
                        RecordType.Reference, AnnotationCode.Create,
                        AnnotationType.Info);
                reference.getAnnotations().add(annotation);
                logger.info("Adding reference " + reference.getBibliographicCitation());
                return reference;
            } else {
                // We've seen this reference before, but not in this chunk

                if ((persistedReference.getModified() != null && reference
                        .getModified() != null)
                        && !persistedReference.getModified().isBefore(
                                reference.getModified())) {
                    // Assume the reference hasn't changed, but maybe this taxon
                    // should be associated with it
                    Taxon taxon = reference.getTaxa().iterator().next();

                    if (persistedReference.getTaxa().contains(taxon)) {
                        // do nothing
                    } else {
                        // Add the taxon to the list of taxa
                        bindReference(persistedReference);
                        logger.info("Updating reference " + reference.getBibliographicCitation());
                        persistedReference.getTaxa().add(taxon);
                        for (Taxon t : reference.getTaxa()) {
                            t.getReferences().add(persistedReference);
                        }
                    }
                    return persistedReference;
                } else {
                    // Assume that this is the first of several times this
                    // reference appears in the result set, and we'll use this
                    // version to overwrite the existing reference
                	for (Annotation annotation : persistedReference.getAnnotations()) {
                      	 if(logger.isInfoEnabled()) {
                       	   logger.info("Comparing " + annotation.getJobId() + " with " + getStepExecution().getJobExecutionId());
                      	 }
                           if (getStepExecution().getJobExecutionId().equals(
                           		annotation.getJobId())) {                         
                               annotation.setType(AnnotationType.Info);
                               annotation.setCode(AnnotationCode.Update);
                               break;
                           }
                    }
                    persistedReference.setCreator(reference.getCreator());
                    persistedReference.setBibliographicCitation(reference.getBibliographicCitation());
                    persistedReference.setCreated(reference.getCreated());
                    persistedReference.setDate(reference.getDate());
                    persistedReference.setSubject(reference.getSubject());
                    persistedReference.setSource(reference.getSource());
                    persistedReference.setDescription(reference.getDescription());
                    persistedReference.setTitle(reference.getTitle());
                    persistedReference.setType(reference.getType());

                    persistedReference.getTaxa().clear();
                    for (Taxon t : reference.getTaxa()) {
                        persistedReference.getTaxa().add(t);
                        if (!t.getReferences().contains(persistedReference)) {
                            t.getReferences().add(persistedReference);
                        }
                    }
                    bindReference(persistedReference);
                    logger.info("Overwriting reference " + persistedReference.getBibliographicCitation());
                    return persistedReference;

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
                for (Taxon t : reference.getTaxa()) {
                    t.getReferences().add(boundReference);
                }
                boundReference.getTaxa().add(taxon);
            }
            // We've already returned this object once
            logger.info("Skipping reference " + reference.getBibliographicCitation());
            return null;
        }
    }

    /**
     *
     * @param reference The reference to bind
     */
    private void bindReference(final Reference reference) {
        if (reference.getIdentifier() != null) {
            boundReferences.put(reference.getIdentifier(), reference);
        }
        if (reference.getBibliographicCitation() != null) {
            boundReferences.put(reference.getBibliographicCitation(), reference);
        }
    }

    /**
     * Retrieve a bound reference, either by identifier or by source.
     * @param reference The reference containing properties to look up
     * @return a reference or NULL if none exist
     */
    private Reference retrieveBoundReference(final Reference reference) {
        if (reference.getIdentifier() != null) {
            return referenceService.find(reference.getIdentifier());
        } else if (reference.getBibliographicCitation() != null) {
            return referenceService.findBySource(reference.getBibliographicCitation());
        }
        return null;
    }

    /**
     * Lookup a bound reference, first by identifier, then by source.
     * @param reference The reference to look up
     * @return a bound reference or null if none is bound
     */
    private Reference lookupBoundReference(final Reference reference) {
        if (reference.getIdentifier() != null) {
            return boundReferences.get(reference.getIdentifier());
        } else if (reference.getBibliographicCitation() != null) {
            return boundReferences.get(reference.getBibliographicCitation());
        }
        return null;
    }

    /**
     * @param references the list of references written
     */
    public void afterWrite(final List<? extends Reference> references) {

    }

    /**
     * @param references the list of references to write
     */
    public void beforeWrite(final List<? extends Reference> references) {

    }

    /**
     * @param exception the exception thrown
     * @param references the list of references
     */
    public void onWriteError(final Exception exception,
            final List<? extends Reference> references) {

    }

    /**
     *
     */
    public void afterChunk() {
    }

    /**
     *
     */
    public final void beforeChunk() {
        boundReferences.clear();
    }
}
