package org.emonocot.job.checklist;

import java.util.Set;

import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.GeographyConverter;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
import org.hibernate.engine.Status;
import org.openarchives.pmh.Record;
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
import org.tdwg.voc.ToTaxon;

/**
 *
 * @author ben
 *
 */
public class OaiPmhRecordProcessor
    implements ItemProcessor<Record, Taxon> {

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

    @Override
    public final Taxon process(final Record record) throws Exception {
        Taxon taxon = taxonService.find(record.getHeader().getIdentifier()
                .toString());

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
                taxon.setName(taxonConcept.getTitle());
                if (taxonConcept.getHasRelationship() != null) {
                    for (Relationship relationship : taxonConcept
                            .getHasRelationship()) {
                        TaxonRelationshipTerm term = resolveRelationshipTerm(relationship
                                .getRelationshipCategoryRelation());
                        Taxon related = resolveRelatedTaxon(relationship
                                .getToTaxonRelation());
                        if (term.equals(TaxonRelationshipTerm.IS_SYONYM_FOR)) {
                            if (!related.getSynonyms().contains(taxon)) {
                                taxon.setAccepted(related);
                            }
                        } else if (term
                                .equals(TaxonRelationshipTerm.HAS_SYNONYM)) {
                            if (!taxon.getSynonyms().contains(related)) {
                                related.setAccepted(taxon);
                            }
                        } else if (term
                                .equals(TaxonRelationshipTerm.IS_CHILD_TAXON_OF)) {
                            if (!related.getChildren().contains(taxon)) {
                                taxon.setParent(related);
                            }
                        } else if (term
                                .equals(TaxonRelationshipTerm.IS_PARENT_TAXON_OF)) {
                            if (!taxon.getChildren().contains(related)) {
                                related.setParent(taxon);
                            }
                        }
                    }
                }

                if (taxonConcept.getDescribedBy() != null) {
                    for (SpeciesProfileModel spm : taxonConcept
                            .getDescribedBy()) {
                        if (spm.getHasInformation() != null) {
                            for (InfoItem infoItem : spm.getHasInformation()) {
                                if (infoItem instanceof Distribution) {
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
        return distribution;
    }

    /**
     *
     * @param toTaxonRelation A to taxon relation
     * @return a local taxon, if already saved, or a new record
     */
    private Taxon resolveRelatedTaxon(final ToTaxon toTaxonRelation) {
        String identifier = null;
        if (toTaxonRelation.getTaxonConcept() != null) {
            identifier = toTaxonRelation.getTaxonConcept().getIdentifier()
                    .toString();
        } else {
            identifier = toTaxonRelation.getResource().toString();
        }
        Taxon taxon = taxonService.find(identifier);
        if (taxon == null) {
            taxon = new Taxon();
            taxon.setIdentifier(identifier);
        }
        return taxon;
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
