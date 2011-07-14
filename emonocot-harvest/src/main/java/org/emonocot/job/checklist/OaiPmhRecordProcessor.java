package org.emonocot.job.checklist;

import java.util.Set;

import org.emonocot.harvest.common.TaxonRelationship;
import org.emonocot.harvest.common.TaxonRelationshipResolver;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.GeographyConverter;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.engine.Status;
import org.openarchives.pmh.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
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
    extends TaxonRelationshipResolver
    implements ItemProcessor<Record, Taxon> {

   /**
    *
    */
    private Logger logger
        = LoggerFactory.getLogger(OaiPmhRecordProcessor.class);

    /**
     *
     */
    private Converter<String, GeographicalRegion>
        geographyConverter = new GeographyConverter();

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

                processTaxon(taxon, taxonConcept);

            }
        } else {
            // We do have a record of this taxon yet
            if (record.getHeader().getStatus() != null
                    && record.getHeader().getStatus().equals(Status.DELETED)) {
                // We have a record of it and now we need to delete it
                taxon.setDeleted(true);
            } else {
                TaxonConcept taxonConcept = record.getMetadata()
                .getTaxonConcept();

                processTaxon(taxon, taxonConcept);
            }
        }


        return taxon;
    }

    /**
     *
     * @param taxon Set the taxon object
     * @param taxonConcept Set the taxonConcept object
     */
    private void processTaxon(
            final Taxon taxon, final TaxonConcept taxonConcept) {
        taxon.setIdentifier(taxonConcept.getIdentifier().toString());
        super.bind(taxon);
        if (taxonConcept.getHasName() != null) {
            logger.info(taxonConcept.getHasName().getNameComplete());
            taxon.setName(taxonConcept.getHasName().getNameComplete());
        } else {
            taxon.setName(taxonConcept.getTitle());
        }
        if (taxonConcept.getHasRelationship() != null) {
            for (Relationship relationship
                : taxonConcept.getHasRelationship()) {
                addRelationship(taxon, relationship);
            }
        }

        if (taxonConcept.getDescribedBy() != null) {
            for (SpeciesProfileModel spm : taxonConcept.getDescribedBy()) {
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
                                taxon.getDistribution().put(dist.getRegion(),
                                        dist);
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
        addTaxonRelationship(new TaxonRelationship(taxon, term), identifier);
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
}
