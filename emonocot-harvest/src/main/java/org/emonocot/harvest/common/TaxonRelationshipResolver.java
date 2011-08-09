package org.emonocot.harvest.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemWriteListener;
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
  implements ChunkListener, ItemWriteListener<Taxon> {

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
   protected TaxonService taxonService;

   /**
    *
    * @param taxonService Set the taxon service
    */
   @Autowired
   public final void setTaxonService(final TaxonService taxonService) {
       this.taxonService = taxonService;
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
                    return taxaWithinChunk.get(identifier);
                } else {
                    Taxon taxon = taxonService.find(identifier,
                            "taxon-with-related");
                    if (taxon == null) {
                        taxon = new Taxon();
                        taxon.setIdentifier(identifier);
                        taxaWithinChunk.put(identifier, taxon);
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
        taxonRelationship.setTo(resolve(identifier));
        this.taxonRelationships.add(taxonRelationship);
    }

    public final void beforeWrite(final List<? extends Taxon> items) {
        logger.info("Before Write");
        for (TaxonRelationship taxonRelationship : taxonRelationships) {
            TaxonRelationshipTerm term = taxonRelationship.getTerm();
            Taxon taxon = taxonRelationship.getFrom();
            Taxon related = null;
            try {
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
    }

    public void afterWrite(final List<? extends Taxon> items) {

    }

    public void onWriteError(
            final Exception exception, final List<? extends Taxon> items) {

    }

    public final void beforeChunk() {
        logger.info("Before Chunk");
        this.taxonRelationships = new HashSet<TaxonRelationship>();
        this.taxaWithinChunk = new HashMap<String, Taxon>();
    }

    public final void afterChunk() {
        logger.info("After Chunk");
    }

}
