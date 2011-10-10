package org.emonocot.job.checklist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.emonocot.harvest.common.TaxonRelationship;
import org.emonocot.harvest.common.TaxonRelationshipResolver;
import org.emonocot.model.source.Source;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.AnnotationType;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.GeographyConverter;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.reference.ReferenceType;
import org.emonocot.model.reference.ReferenceTypeConverter;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.RankConverter;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.api.ReferenceService;
import org.openarchives.pmh.Record;
import org.openarchives.pmh.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.tdwg.voc.DefinedTermLinkType;
import org.tdwg.voc.Distribution;
import org.tdwg.voc.InfoItem;
import org.tdwg.voc.PublicationCitation;
import org.tdwg.voc.Relationship;
import org.tdwg.voc.RelationshipCategory;
import org.tdwg.voc.SpeciesProfileModel;
import org.tdwg.voc.TaxonConcept;
import org.tdwg.voc.TaxonName;
import org.tdwg.voc.TaxonRelationshipTerm;

/**
 *
 * @author ben
 *
 */
public class OaiPmhRecordProcessorImpl extends TaxonRelationshipResolver
    implements OaiPmhRecordProcessor {

   /**
    *
    */
    private Logger logger
        = LoggerFactory.getLogger(OaiPmhRecordProcessorImpl.class);

    /**
     *
     */
    private Converter<String, GeographicalRegion>
        geographyConverter = new GeographyConverter();

    /**
     *
     */
    private Converter<String, ReferenceType>
        referenceTypeConverter = new ReferenceTypeConverter();

    /**
     *
     */
    private Converter<String, Rank> rankConverter = new RankConverter();

    /**
     *
     */
    private ReferenceService referenceService;

    /**
     *
     */
    private Map<String, Reference> referencesWithinChunk
        = new HashMap<String, Reference>();

    /**
     *
     * @param referenceService Set the reference service
     */
    public final void setReferenceService(ReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    /**
     * @param record an OAI-PMH Record object
     * @return a taxon object
     * @throws Exception if there is a problem processing this record
     */
    public final Taxon process(final Record record) throws Exception {
        Taxon taxon = taxonService.find(record.getHeader().getIdentifier()
                .toString(), "taxon-with-related");

        if (taxon == null) {
            // We don't have a record of this taxon yet
            if (record.getHeader().getStatus() != null
                    && record.getHeader().getStatus().equals(Status.deleted)) {
                // It was created and then deleted in between harvesting - so
                // ignore.
                return null;
            } else {
                // Create a new taxon
                taxon = new Taxon();
                TaxonConcept taxonConcept = record.getMetadata()
                        .getTaxonConcept();
                Annotation annotation = new Annotation();
                annotation.setAnnotatedObjType("Taxon");
                annotation.setJobId(getStepExecution().getJobExecutionId());
                annotation.setCode("Created");
                annotation.setType(AnnotationType.Create);
                annotation.setSource(getSource());
                taxon.getAnnotations().add(annotation);
                taxon.getSources().add(getSource());
                taxon.setAuthority(getSource());
                processTaxon(taxon, taxonConcept);
            }
        } else {
            // We do have a record of this taxon yet
            if (record.getHeader().getStatus() != null
                    && record.getHeader().getStatus().equals(Status.deleted)) {
                // We have a record of it and now we need to delete it
                taxon.setDeleted(true);
            } else {
                TaxonConcept taxonConcept = record.getMetadata()
                .getTaxonConcept();
                Annotation annotation = new Annotation();
                annotation.setAnnotatedObjType("Taxon");
                annotation.setJobId(getStepExecution().getJobExecutionId());
                annotation.setType(AnnotationType.Update);
                annotation.setCode("Updated");
                annotation.setSource(getSource());
                taxon.getAnnotations().add(annotation);
                /**
                 * Using java.util.Collection.contains() does not work on lazy
                 * collections.
                 */
                boolean contains = false;
                for (Source auth : taxon.getSources()) {
                    if (auth.equals(getSource())) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    taxon.getSources().add(getSource());
                }

                taxon.setAuthority(getSource());
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
    public final void processTaxon(
            final Taxon taxon, final TaxonConcept taxonConcept) {
        taxon.setIdentifier(taxonConcept.getIdentifier().toString());
        super.bind(taxon);
        if (taxonConcept.getHasName() != null) {
            TaxonName taxonName = taxonConcept.getHasName();
            logger.info(taxonName.getNameComplete());
            taxon.setName(taxonName.getNameComplete());
            taxon.setAuthorship(taxonName.getAuthorship());
            taxon.setBasionymAuthorship(
                    taxonName.getBasionymAuthorship());
            taxon.setFamily(taxonName.getFamily());
            taxon.setUninomial(taxonName.getUninomial());
            taxon.setGenus(taxonName.getGenusPart());
            taxon.setSpecificEpithet(
                    taxonName.getSpecificEpithet());
            taxon.setInfraSpecificEpithet(
                    taxonName.getInfraSpecificEpithet());
            if (taxonName.getPublishedInCitations() != null
                    && !taxonName.getPublishedInCitations().isEmpty()) {
                PublicationCitation protologue = taxonName
                        .getPublishedInCitations().iterator().next();
                String referenceIdentifier = protologue
                    .getIdentifier().toString();
                Reference reference = null;
                if (referencesWithinChunk.containsKey(referenceIdentifier)) {
                    reference = referencesWithinChunk.get(referenceIdentifier);
                } else {
                    reference = referenceService.find(referenceIdentifier);
                    referencesWithinChunk.put(referenceIdentifier, reference);
                }
                if (reference == null) {
                    // We've not seen this before
                    reference = new Reference();
                    reference.setIdentifier(referenceIdentifier);
                    referencesWithinChunk.put(referenceIdentifier, reference);
                }
                // TODO Created / modified dates on publications? Bridge too far?
                reference.setTitle(protologue.getTpubTitle());
                reference.setVolume(protologue.getVolume());
                reference.setPages(protologue.getPages());
                reference.setDate(protologue.getDatePublished());
                if (protologue.getPublicationType() != null
                        && protologue.getPublicationType()
                        .getIdentifier() != null) {
                    reference.setType(referenceTypeConverter.convert(protologue
                            .getPublicationType().getIdentifier().toString()));
                }
                taxon.setProtologue(reference);

            }
            try {
                taxon.setRank(rankConverter.convert(
                        taxonName.getRankString()));
            } catch (IllegalArgumentException iae) {
                Annotation annotation = new Annotation();
                annotation.setAnnotatedObjType("Taxon");
                annotation.setJobId(getStepExecution().getJobExecutionId());
                annotation.setType(AnnotationType.Warn);
                annotation.setCode("rank");
                annotation.setText(iae.getMessage());
                annotation.setSource(getSource());
                taxon.getAnnotations().add(annotation);
            }

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
                                    .getHasValueRelation(), taxon);
                            if (dist.getRegion() != null
                                    && !taxon.getDistribution().keySet()
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
    *
    */
    @Override
   public final void beforeChunk() {
        this.referencesWithinChunk = new HashMap<String, Reference>();
        super.beforeChunk();
   }

    /**
     * @param items the items to be written
     */
    public final void beforeWrite(final List<? extends Taxon> items) {
        super.beforeWrite(items);
        this.referencesWithinChunk.clear();
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
        if (relationship.getToTaxonRelation() != null
                && relationship.getToTaxonRelation().getTaxonConcept() != null
                && relationship.getToTaxonRelation().getTaxonConcept()
                        .getIdentifier() != null) {
            identifier = relationship.getToTaxonRelation().getTaxonConcept()
                    .getIdentifier().toString();
        } else if (relationship.getToTaxonRelation() != null
                && relationship.getToTaxonRelation().getResource() != null) {
            identifier = relationship.getToTaxonRelation().getResource()
                    .toString();
        }

        if (identifier != null) {
            TaxonRelationshipTerm term = resolveRelationshipTerm(relationship
                .getRelationshipCategoryRelation());
            TaxonRelationship taxonRelationship = new TaxonRelationship(taxon,
                    term);
            taxonRelationship.setToIdentifier(identifier);
            addTaxonRelationship(taxonRelationship, identifier);
        } else {
            Annotation annotation = new Annotation();
            annotation.setAnnotatedObjType("Taxon");
            annotation.setJobId(getStepExecution().getJobExecutionId());
            annotation.setType(AnnotationType.Warn);
            annotation.setCode("related");
            annotation.setText("Could not find identifier for relationship of taxon "
                    + taxon.getIdentifier());
            annotation.setSource(getSource());
            taxon.getAnnotations().add(annotation);
        }
    }

    /**
     *
     * @param hasValueRelation a has value relation
     * @param taxon Set the taxon
     * @return a valid Distribution
     */
    private org.emonocot.model.description.Distribution resolveDistribution(
            final Set<DefinedTermLinkType> hasValueRelation, final Taxon taxon) {
        // TODO - what if there are no terms or multiple terms - throw an error?
        GeographicalRegion region = null;
        if (hasValueRelation == null || hasValueRelation.isEmpty()) {
            Annotation annotation = new Annotation();
            annotation.setAnnotatedObjType("Taxon");
            annotation.setJobId(getStepExecution().getJobExecutionId());
            annotation.setType(AnnotationType.Warn);
            annotation.setCode("distribution");
            annotation.setText("No geographical term returned"
                    + taxon.getIdentifier());
            annotation.setSource(getSource());
            taxon.getAnnotations().add(annotation);
            return null;
        }
        DefinedTermLinkType definedTermLinkType = hasValueRelation.iterator()
                .next();
        if (definedTermLinkType.getDefinedTerm() != null) {
            region = geographyConverter.convert(definedTermLinkType
                    .getDefinedTerm().getIdentifier().toString());
        } else if (definedTermLinkType.getResource() != null) {
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
