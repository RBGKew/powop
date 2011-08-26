package org.emonocot.harvest.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.emonocot.model.common.Annotation;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.tdwg.voc.TaxonRelationshipTerm;

/**
 * Class which encapsulates logic to handle the processing of
 * taxonomic relationships within spring batch.
 *
 * @author ben
 *
 */
public abstract class TaxonRelationshipResolver
 implements ChunkListener,
        StepExecutionListener, ItemWriteListener<Taxon> {
    /**
     *
     */
    private StepExecution stepExecution;

   /**
    *
    */
    private Logger logger
      = LoggerFactory.getLogger(TaxonRelationshipResolver.class);

   /**
    *
    */
    private Map<String, Taxon> taxaWithinChunk = new HashMap<String, Taxon>();

   /**
    *
    */
    private Set<TaxonRelationship> taxonRelationships
        = new HashSet<TaxonRelationship>();

   /**
    *
    */
    private Map<String, Set<TaxonRelationship>> inverseRelationships
        = new HashMap<String, Set<TaxonRelationship>>();

   /**
    *
    */
   protected TaxonService taxonService;

   /**
    *
    * @param taxonService Set the taxon service
    */
   public final void setTaxonService(final TaxonService taxonService) {
       this.taxonService = taxonService;
   }

   /**
    *
    * @return the inverse relationships
    */
   public final Map<String,Set<TaxonRelationship>> getInverseRelationships() {
       return inverseRelationships;
   }

   /**
    *
    * @param identifier the identifier of the taxon you want to resolve
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
                        annotation.setAnnotatedObjType("Taxon");
                        annotation.setJobId(
                                getStepExecution().getJobExecutionId());
                        annotation.setCode("Created");
                        taxon.getAnnotations().add(annotation);
                        taxon.setIdentifier(identifier);
                        logger.info("Didn't taxon with identifier " + identifier
                                + " from service returning new taxon");
                        taxaWithinChunk.put(identifier, taxon);
                    } else {
                        logger.info("Found taxon with identifier " + identifier
                                + " from service returning taxon with id "
                                + taxon.getId());
                        taxaWithinChunk.put(taxon.getIdentifier(), taxon);
                        if (taxon.getParent() != null
                                && !taxaWithinChunk.containsKey(taxon
                                        .getParent().getIdentifier())) {
                            logger.info("Binding " + taxon.getParent() + " with id " + taxon.getParent().getId() + " and identifier " + taxon.getParent().getIdentifier());
                            taxaWithinChunk.put(taxon.getParent()
                                    .getIdentifier(), taxon.getParent());
                        }
                        if (taxon.getAccepted() != null
                                && !taxaWithinChunk.containsKey(taxon
                                        .getAccepted().getIdentifier())) {
                            logger.info("Binding " + taxon.getAccepted() + " with id " + taxon.getAccepted().getId() + " and identifier " + taxon.getAccepted().getIdentifier());
                            taxaWithinChunk.put(taxon.getAccepted()
                                    .getIdentifier(), taxon.getAccepted());
                        }
                        for (Taxon child : taxon.getChildren()) {
                            if (!taxaWithinChunk.containsKey(child
                                    .getIdentifier())) {
                                logger.info("Binding " + child + " with id " + child.getId() + " and identifier " + child.getIdentifier());
                                taxaWithinChunk.put(child.getIdentifier(),
                                        child);
                            }
                        }
                        for (Taxon synonym : taxon.getSynonyms()) {
                            if (!taxaWithinChunk.containsKey(synonym
                                    .getIdentifier())) {
                                logger.info("Binding " + synonym + " with id " + synonym.getId() + " and identifier " + synonym.getIdentifier());
                                taxaWithinChunk.put(synonym.getIdentifier(),
                                        synonym);
                            }
                        }
                    }
                    return taxon;
                }
            }
        };
    }

    /**
     *
     * @param taxon
     *            The taxon itself
     */
    public final void bind(final Taxon taxon) {
        logger.info("Binding " + taxon + " with id " + taxon.getId() + " and identifier " + taxon.getIdentifier());
        taxaWithinChunk.put(taxon.getIdentifier(), taxon);
    }

    /**
     *
     * @param taxonRelationship Add a taxon relationship
     * @param identifier Set the identifier of the 'to' taxon
     */
    public final void addTaxonRelationship(
            final TaxonRelationship taxonRelationship,
            final String identifier) {
        boolean containsInverse = false;
        if (inverseRelationships.containsKey(identifier)) {
            for (TaxonRelationship relationship : inverseRelationships
                    .get(identifier)) {
                if (relationship.getToIdentifier().equals(
                        taxonRelationship.getFrom().getIdentifier())) {
                    containsInverse = true;
                    break;
                }
            }
        }
        if (!containsInverse) {
            taxonRelationship.setTo(resolve(identifier));
            taxonRelationship.setToIdentifier(identifier);
            this.taxonRelationships.add(taxonRelationship);
        } else {
            logger.info("Resolver is aware of inverse relationship between " + taxonRelationship.getFrom().getIdentifier() + " and " + identifier + "ignoring");
        }
    }

    /**
     * @param items the items to be written
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
            if (term.equals(TaxonRelationshipTerm.IS_SYNONYM_FOR)) {
                if (!related.getSynonyms().contains(taxon)) {
                    taxon.setAccepted(related);
                    related.getSynonyms().add(taxon);
                }
            } else if (term.equals(TaxonRelationshipTerm.HAS_SYNONYM)) {
                if (!taxon.getSynonyms().contains(related)) {
                    related.setAccepted(taxon);
                    taxon.getSynonyms().add(related);
                }
            } else if (term.equals(TaxonRelationshipTerm.IS_CHILD_TAXON_OF)) {
                if (!related.getChildren().contains(taxon)) {
                    taxon.setParent(related);
                    related.getChildren().add(taxon);
                }
            } else if (term.equals(TaxonRelationshipTerm.IS_PARENT_TAXON_OF)) {
                if (!taxon.getChildren().contains(related)) {
                    related.setParent(taxon);
                    taxon.getChildren().add(related);
                }
            }
        }

        taxaWithinChunk.clear();
        taxonRelationships.clear();
        inverseRelationships.clear();
    }

    /**
     * @param items the list of items written
     */
    public final void afterWrite(final List<? extends Taxon> items) {

    }

    /**
     * @param exception the exception thrown
     * @param items the items which we tried to write
     */
    public void onWriteError(
            final Exception exception, final List<? extends Taxon> items) {

    }

    /**
     *
     */
    public void beforeChunk() {
        logger.info("Before Chunk");
        this.taxonRelationships = new HashSet<TaxonRelationship>();
        this.inverseRelationships = new HashMap<String,Set<TaxonRelationship>>();
        this.taxaWithinChunk = new HashMap<String, Taxon>();
    }

    /**
     *
     */
    public final void afterChunk() {
        logger.info("After Chunk");
    }

    /**
     * @param newStepExecution Set the step exectuion
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
     * @return the step execution
     */
    public final StepExecution getStepExecution() {
        return stepExecution;
    }
}
