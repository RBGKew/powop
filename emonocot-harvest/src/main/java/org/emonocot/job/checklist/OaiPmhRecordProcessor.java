package org.emonocot.job.checklist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.GeographyConverter;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
import org.hibernate.engine.Status;
import org.openarchives.pmh.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.tdwg.voc.DefinedTermLinkType;
import org.tdwg.voc.Distribution;
import org.tdwg.voc.InfoItem;
import org.tdwg.voc.Relationship;
import org.tdwg.voc.RelationshipCategory;
import org.tdwg.voc.SpeciesProfileModel;
import org.tdwg.voc.TaxonConcept;
import org.tdwg.voc.TaxonRelationshipTerm;

/**
 *
 * @author ben
 *
 */
public class OaiPmhRecordProcessor
    implements ItemProcessor<Record, Taxon>, ChunkListener, ItemWriteListener<Taxon> {

   /**
    *
    */
    private Logger logger
        = LoggerFactory.getLogger(OaiPmhRecordProcessor.class);

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
    private Converter<String, GeographicalRegion>
        geographyConverter = new GeographyConverter();

   /**
    *
    */
   private TaxonService taxonService;

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
   * @param taxon The taxon itself
   */
  public final void bind(final Taxon taxon) {
      taxaWithinChunk.put(taxon.getIdentifier(), taxon);
  }

    @Override
    public final Taxon process(final Record record) throws Exception {
        Taxon taxon = taxonService.find(record.getHeader().getIdentifier()
                .toString(), "taxon-with-related");

        if (taxon == null) {
            // We don't have a record of this taxon yet
            if (record.getHeader().getStatus() != null
                    && record.getHeader().getStatus().equals(Status.DELETED)) {
                // It was created and then deleted in between harvesting - so
                // ignore.
                return null;
            } else {
                // Create a new taxon
                taxon = new Taxon();
                TaxonConcept taxonConcept = record.getMetadata()
                        .getTaxonConcept();

                taxon.setIdentifier(taxonConcept.getIdentifier().toString());
                bind(taxon);
                if (taxonConcept.getHasName() != null) {
                    taxon.setName(taxonConcept.getHasName().getNameComplete());
                } else {
                    taxon.setName(taxonConcept.getTitle());
                }
                if (taxonConcept.getHasRelationship() != null) {
                    for (Relationship relationship : taxonConcept
                            .getHasRelationship()) {
                        addRelationship(taxon, relationship);
                    }
                }

                if (taxonConcept.getDescribedBy() != null) {
                    for (SpeciesProfileModel spm : taxonConcept
                            .getDescribedBy()) {
                        if (spm.getHasInformation() != null) {
                            logger.info("hasInformation " + spm.getHasInformation());
                            for (InfoItem infoItem : spm.getHasInformation()) {
                                logger.info("hasInformation " + infoItem);
                                if (infoItem instanceof Distribution) {
                                    logger.info("hasInformation = Distribution");
                                    Distribution distribution = (Distribution) infoItem;
                                    org.emonocot.model.description.Distribution dist
                                      = resolveDistribution(distribution
                                            .getHasValueRelation());
                                    if (!taxon.getDistribution().keySet()
                                            .contains(dist.getRegion())) {
                                        dist.setTaxon(taxon);
                                        taxon.getDistribution().put(
                                                dist.getRegion(), dist);
                                    } else {
                                        // TODO replace / update distribution if
                                        // neccessary
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } else {
         // We don't have a record of this taxon yet
            if (record.getHeader().getStatus() != null
                    && record.getHeader().getStatus().equals(Status.DELETED)) {
                // We have a record of it and now we need to delete it
                taxon.setDeleted(true);
            } else {
                // We have a record of the taxon and now we should update it
            }
        }


        return taxon;
    }

    /**
     * Adds a relationship to a list of relationships which should be resolved
     * once the taxa in this chunk have been processed. Allows for forward
     * references within the chunk to prevent duplicate entries if a taxon is
     * proceeded by a related taxon within a chunk.
     *
     * @param taxon Set the from taxon.
     * @param relationship Set the relationship object.
     */
    private void addRelationship(final Taxon taxon,
            final Relationship relationship) {
        String identifier = null;
        if (relationship.getToTaxonRelation().getTaxonConcept() != null) {
            identifier = relationship.getToTaxonRelation().getTaxonConcept()
                    .getIdentifier().toString();
        } else {
            identifier = relationship.getToTaxonRelation().getResource()
                    .toString();
        }

        TaxonRelationshipTerm term = resolveRelationshipTerm(relationship
                .getRelationshipCategoryRelation());
        this.taxonRelationships.add(new TaxonRelationship(taxon, term,
                resolve(identifier)));
    }

    /**
     *
     * @param hasValueRelation a has value relation
     * @return a valid Distribution
     */
    private org.emonocot.model.description.Distribution resolveDistribution(
            final Set<DefinedTermLinkType> hasValueRelation) {
        // TODO - what if there are no terms or multiple terms - throw an error?
        GeographicalRegion region = null;
        DefinedTermLinkType definedTermLinkType = hasValueRelation.iterator()
                .next();
        if (definedTermLinkType.getDefinedTerm() != null) {
            region = geographyConverter.convert(definedTermLinkType
                    .getDefinedTerm().getIdentifier().toString());
        } else {
            region = geographyConverter.convert(definedTermLinkType
                    .getResource().toString());
        }
        org.emonocot.model.description.Distribution distribution
           = new org.emonocot.model.description.Distribution();
        distribution.setRegion(region);
        logger.info("Resolving " + definedTermLinkType
                + " returning " + region);
        return distribution;
    }

    /**
     *
     * @param relationshipCategory the relationship category
     * @return a taxon relationship term
     */
    private TaxonRelationshipTerm resolveRelationshipTerm(
            final RelationshipCategory relationshipCategory) {
        if (relationshipCategory.getTaxonRelationshipTerm() != null) {
            return relationshipCategory.getTaxonRelationshipTerm();
        } else {
            return TaxonRelationshipTerm.fromValue(relationshipCategory
                    .getResource().toString());
        }
    }

    @Override
    public final void beforeChunk() {
        logger.info("Before Chunk");
        this.taxonRelationships = new HashSet<TaxonRelationship>();
        this.taxaWithinChunk = new HashMap<String, Taxon>();
    }

    @Override
    public final void afterChunk() {
    	logger.info("After Chunk");
    }

    /**
     *
     * @author ben
     *
     */
    class TaxonRelationship {
        /**
         *
         */
        private Taxon from;
        /**
         *
         */
        private TaxonRelationshipTerm term;
        /**
         *
         */
        private Callable<Taxon> to;

        /**
         *
         * @param newFrom Set the from taxon
         * @param newTerm Set relationship type
         * @param newTo Set a callable representing the to taxon
         */
        TaxonRelationship(final Taxon newFrom,
                final TaxonRelationshipTerm newTerm,
                final Callable<Taxon> newTo) {
            this.from = newFrom;
            this.term = newTerm;
            this.to = newTo;
        }
    }

	@Override
	public void afterWrite(List<? extends Taxon> results) {
		
	}

	@Override
	public void beforeWrite(List<? extends Taxon> results) {
		logger.info("Before Write");
        for (TaxonRelationship taxonRelationship : taxonRelationships) {
            TaxonRelationshipTerm term = taxonRelationship.term;
            Taxon taxon = taxonRelationship.from;
            Taxon related = null;
            try {
                related = taxonRelationship.to.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (term.equals(TaxonRelationshipTerm.IS_SYONYM_FOR)) {
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

	@Override
	public void onWriteError(Exception exception,
			List<? extends Taxon> results) {
		
	}
}
