package org.emonocot.job.dwc.reference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.emonocot.api.ReferenceService;
import org.emonocot.job.dwc.DarwinCoreValidator;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;
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
public class ReferenceValidator extends DarwinCoreValidator<Reference>
        implements ChunkListener, ItemWriteListener<Reference> {
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
                logger.info("Adding reference " + reference.getSource());
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
                        logger.info("Updating reference " + reference.getSource());
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
                    persistedReference.setNumber(reference.getNumber());
                    persistedReference.setAuthor(reference.getAuthor());
                    persistedReference.setCitation(reference.getCitation());
                    persistedReference.setCreated(reference.getCreated());
                    persistedReference.setDate(reference.getDatePublished());
                    persistedReference.setKeywords(reference.getKeywords());
                    persistedReference.setPages(reference.getPages());
                    persistedReference.setPublishedIn(reference.getPublishedIn());
                    persistedReference.setReferenceAbstract(reference.getReferenceAbstract());
                    persistedReference.setTitle(reference.getTitle());
                    persistedReference.setType(reference.getType());
                    persistedReference.setVolume(reference.getVolume());

                    persistedReference.getTaxa().clear();
                    for (Taxon t : reference.getTaxa()) {
                        persistedReference.getTaxa().add(t);
                        if (!t.getReferences().contains(persistedReference)) {
                            t.getReferences().add(persistedReference);
                        }
                    }
                    Annotation annotation = createAnnotation(persistedReference,
                            RecordType.Reference, AnnotationCode.Update,
                            AnnotationType.Info);
                    persistedReference.getAnnotations().add(annotation);
                    bindReference(persistedReference);
                    logger.info("Overwriting reference " + persistedReference.getSource());
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
            logger.info("Skipping reference " + reference.getSource());
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
        if (reference.getSource() != null) {
            boundReferences.put(reference.getSource(), reference);
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
        } else if (reference.getSource() != null) {
            return referenceService.findBySource(reference.getSource());
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
        } else if (reference.getSource() != null) {
            return boundReferences.get(reference.getSource());
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
