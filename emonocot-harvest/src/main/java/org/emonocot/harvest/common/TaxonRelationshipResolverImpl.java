package org.emonocot.harvest.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.emonocot.api.TaxonService;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationCode;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.common.RecordType;
import org.emonocot.model.taxon.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecutionListener;
import org.tdwg.voc.TaxonRelationshipTerm;

/**
 * Class which encapsulates logic to handle the processing of taxonomic
 * relationships within spring batch.
 *
 * @author ben
 *
 */
public class TaxonRelationshipResolverImpl extends AuthorityAware
        implements ChunkListener, StepExecutionListener,
        ItemWriteListener<Taxon>, TaxonRelationshipResolver {
    /**
    *
    */
    private Logger logger = LoggerFactory
            .getLogger(TaxonRelationshipResolverImpl.class);

    /**
    *
    */
    private Map<String, Taxon> taxaWithinChunk = new HashMap<String, Taxon>();

   /**
    *
    */
    private Set<TaxonRelationship> taxonRelationships = new HashSet<TaxonRelationship>();

   /**
    *
    */
    protected TaxonService taxonService;

    /**
     *
     * @param newTaxonService
     *            Set the taxon service
     */
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    /**
     *
     * @param identifier
     *            the identifier of the taxon you want to resolve
     * @return A callable which resolves to a Taxon
     */
    public final Callable<Taxon> resolve(final String identifier) {
        return new Callable<Taxon>() {
            public Taxon call() {
                if (taxaWithinChunk.containsKey(identifier)) {
                    logger.info("Found taxon with identifier " + identifier
                            + " from cache returning taxon with id "
                            + taxaWithinChunk.get(identifier).getId());
                    return taxaWithinChunk.get(identifier);
                } else {
                    Taxon taxon = taxonService.find(identifier,
                            "taxon-with-related");

                    if (taxon == null) {
                        taxon = new Taxon();
                        Annotation annotation = new Annotation();
                        annotation.setAnnotatedObj(taxon);
                        annotation.setJobId(getStepExecution()
                                .getJobExecutionId());
                        annotation.setCode(AnnotationCode.Create);
                        annotation.setRecordType(RecordType.Taxon);
                        annotation.setType(AnnotationType.Info);
                        annotation.setSource(getSource());
                        taxon.getAnnotations().add(annotation);
                        taxon.getSources().add(getSource());
                        taxon.setAuthority(getSource());
                        taxon.setIdentifier(identifier);
                        logger.info("Didn't find taxon with identifier "
                                + identifier
                                + " from service returning new taxon");
                        taxaWithinChunk.put(identifier, taxon);
                    } else {
                        logger.info("Found taxon with identifier " + identifier
                                + " from service returning taxon with id "
                                + taxon.getId());
                        taxaWithinChunk.put(taxon.getIdentifier(), taxon);
                        /*if (taxon.getParent() != null
                                && !taxaWithinChunk.containsKey(taxon
                                        .getParent().getIdentifier())) {
                            logger.info("Binding " + taxon.getParent()
                                    + " with id " + taxon.getParent().getId()
                                    + " and identifier "
                                    + taxon.getParent().getIdentifier());
                            taxaWithinChunk.put(taxon.getParent()
                                    .getIdentifier(), taxon.getParent());
                        }
                        if (taxon.getAccepted() != null
                                && !taxaWithinChunk.containsKey(taxon
                                        .getAccepted().getIdentifier())) {
                            logger.info("Binding " + taxon.getAccepted()
                                    + " with id " + taxon.getAccepted().getId()
                                    + " and identifier "
                                    + taxon.getAccepted().getIdentifier());
                            taxaWithinChunk.put(taxon.getAccepted()
                                    .getIdentifier(), taxon.getAccepted());
                        }
                        for (Taxon child : taxon.getChildren()) {
                            if (!taxaWithinChunk.containsKey(child
                                    .getIdentifier())) {
                                logger.info("Binding " + child + " with id "
                                        + child.getId() + " and identifier "
                                        + child.getIdentifier());
                                taxaWithinChunk.put(child.getIdentifier(),
                                        child);
                            }
                        }
                        for (Taxon synonym : taxon.getSynonyms()) {
                            if (!taxaWithinChunk.containsKey(synonym
                                    .getIdentifier())) {
                                logger.info("Binding " + synonym + " with id "
                                        + synonym.getId() + " and identifier "
                                        + synonym.getIdentifier());
                                taxaWithinChunk.put(synonym.getIdentifier(),
                                        synonym);
                            }
                        }*/
                    }
                    return taxon;
                }
            }
        };
    }

    /**
     * @param taxon Bind the taxon
     */
    public final void bind(final Taxon taxon) {
        logger.info("Binding " + taxon + " with id " + taxon.getId()
                + " and identifier " + taxon.getIdentifier());
        taxaWithinChunk.put(taxon.getIdentifier(), taxon);
    }

    /**
     * @param taxonRelationship Set the taxon relationship
     * @param identifier Set the identifier of the taxon owning the relationship
     */
    public final void addTaxonRelationship(
            final TaxonRelationship taxonRelationship, final String identifier) {  
        taxonRelationship.setTo(resolve(identifier));
        taxonRelationship.setToIdentifier(identifier);
        this.taxonRelationships.add(taxonRelationship);
    }

    /**
     * @param items
     *            the items to be written
     */
    public void beforeWrite(final List<? extends Taxon> items) {
        logger.info("Before Write");
        for (TaxonRelationship taxonRelationship : taxonRelationships) {
            TaxonRelationshipTerm term = taxonRelationship.getTerm();
            Taxon taxon = taxonRelationship.getFrom();
            Taxon related = null;
            try {
                logger.info("Resolving taxon with identifier "
                        + taxonRelationship.getToIdentifier()
                        + " related to taxon " + taxon);
                related = taxonRelationship.getTo().call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(related == null) {
                // TODO log error            
            } else if (term.equals(TaxonRelationshipTerm.IS_SYNONYM_FOR)) {
                if (!related.getSynonyms().contains(taxon)) {
                    taxon.setAccepted(related);
                    related.getSynonyms().add(taxon);
                }
            } else if (term.equals(TaxonRelationshipTerm.IS_CHILD_TAXON_OF)) {
                if (!related.getChildren().contains(taxon)) {
                    taxon.setParent(related);
                    related.getChildren().add(taxon);
                }
            }
        }

        taxaWithinChunk.clear();
        taxonRelationships.clear();
    }

    /**
     * @param items
     *            the list of items written
     */
    public final void afterWrite(final List<? extends Taxon> items) {

    }

    /**
     * @param exception
     *            the exception thrown
     * @param items
     *            the items which we tried to write
     */
    public void onWriteError(final Exception exception,
            final List<? extends Taxon> items) {

    }

    /**
     *
     */
    public final void beforeChunk() {
        logger.info("Before Chunk");
        this.taxonRelationships = new HashSet<TaxonRelationship>();
        this.taxaWithinChunk = new HashMap<String, Taxon>();
    }

    /**
     *
     */
    public final void afterChunk() {
        logger.info("After Chunk");
    }

    /**
     * @param taxon Update the taxon to bind to
     */
    public final void updateTaxon(final Taxon taxon) {
        for (TaxonRelationship relationship : taxonRelationships) {
            if(relationship.getFrom().getIdentifier().equals(taxon.getIdentifier())) {
                relationship.setFrom(taxon);
            }
        }
    }
}
